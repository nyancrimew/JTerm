package ch.deletescape.jterm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.deletescape.jterm.commandcontexts.scripting.Scripting;
import ch.deletescape.jterm.config.Resources;
import ch.deletescape.jterm.config.UserProperties;
import ch.deletescape.jterm.io.Printer;

public final class CommandUtils {

  public static final Map<String, CommandExecutor> COMMAND_LISTENERS = new HashMap<>();
  public static final Set<String> BASE_COMMANDS = new HashSet<>();
  public static final Set<String> CONTEXTS = new HashSet<>();
  private static final Pattern INLINE_COMMAND_PATTERN = Pattern.compile("\\$\\{.(?:.(?!\\$\\{))*?\\}");
  private static final Logger LOGGER = LogManager.getLogger();

  private CommandUtils() {
  }

  static void initializeEnv() throws IOException {
    try (InputStreamReader in = new InputStreamReader(CommandUtils.class.getResourceAsStream("/contexts.ctx"))) {
      BufferedReader br = new BufferedReader(in);
      loadContextsFromBufferedReader(br, CommandUtils.class.getClassLoader());
    }
    loadPlugins();
    BASE_COMMANDS.addAll(COMMAND_LISTENERS.keySet());
    Printer.out.mute(true);
    Path jtermrc = Paths.get(JTerm.getHome(), ".jtermrc");
    if (Files.exists(jtermrc)) {
      Scripting.run(jtermrc.toString());
    }
    Printer.out.mute(false);
    UserProperties.init();
  }

  public static Object evaluateCommand(String input) {
    if (input.startsWith("#")) {
      return null;
    }
    try {
      if (input.startsWith("./")) {
        return Scripting.run(input);
      }
      String cmd = getCmd(input);
      CommandExecutor executor = COMMAND_LISTENERS.get(cmd);
      if (executor != null) {
        return executor.exec(getArgs(input));
      }
      Printer.err.println(Resources.getString("CommandUtils.UnknownCommand"), cmd);
      Printer.out.println(Resources.getString("CommandUtils.UnknownCommandInfo"));
    } catch (Exception e) {
      Printer.err.println(Resources.getString("CommandUtils.Error"), e.toString());
      LOGGER.error("Command \"" + input + "\" threw Exception: " + e.toString(), e);
    }
    return null;
  }

  public static void addListener(String cmd, CommandExecutor exec) {
    COMMAND_LISTENERS.put(cmd, exec);
  }

  public static String parseInlineCommands(String str) {
    boolean wasMuted = Printer.out.mute(true);
    for (Matcher m = INLINE_COMMAND_PATTERN.matcher(str); m.find(); m = INLINE_COMMAND_PATTERN.matcher(str)) {
      Object cmdResult = CommandUtils.evaluateCommand(m.group().replaceAll("^\\$\\{|\\}$", ""));
      str = str.substring(0, m.start()) + cmdResult + str.substring(m.end());
    }
    Printer.out.mute(wasMuted);
    return str;
  }

  static void printInputPromt() {
    String formattedPath = JTerm.getCurrPath().toString().replace(JTerm.getHome(), "~");
    Printer.out.forced().print("%s %s > ", JTerm.getUser(), formattedPath);
  }

  static String getCmd(String cmd) {
    int commandEnd = cmd.indexOf(' ');
    return commandEnd != -1 ? cmd.substring(0, commandEnd) : cmd;
  }

  static String getArgs(String cmd) {
    int indx = cmd.indexOf(' ');
    return indx != -1 ? cmd.substring(indx + 1).trim() : "";
  }

  private static void initializeClass(String clazz, ClassLoader cl) {
    try {
      cl.loadClass(clazz).newInstance();
    } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
      Printer.err.println(Resources.getString("CommandUtils.ErrorLoadingContext"), clazz, e);
      LOGGER.error("Failed to load context " + clazz, e);
    }
  }

  private static void loadContextsFromBufferedReader(BufferedReader br, ClassLoader cl) throws IOException {
    String context;
    while ((context = br.readLine()) != null) {
      context = context.trim();
      if (!context.startsWith("#")) {
        initializeClass(context, cl);
        CONTEXTS.add(context);
      }
    }
  }

  private static void loadPlugins() throws IOException {
    List<URL> urls = new ArrayList<>();
    Path pluginDir = UserProperties.getJtermDir().resolve("plugins");
    try (Stream<Path> stream = Files.list(pluginDir)) {
      stream.filter(p -> p.toString().endsWith(".jar")).forEach(p -> urls.add(pathToURL(p)));
      try (URLClassLoader cl = URLClassLoader.newInstance(urls.toArray(new URL[urls.size()]))) {
        try (BufferedReader br = Files.newBufferedReader(pluginDir.resolve("contexts.ctx"))) {
          loadContextsFromBufferedReader(br, cl);
        }
      }
    } catch (Exception e) {
      LOGGER.error(e.toString(), e);
    }
  }

  private static URL pathToURL(Path p) {
    try {
      return p.toUri().toURL();
    } catch (MalformedURLException e) {
      LOGGER.error(e.toString(), e);
    }
    return null;
  }
}
