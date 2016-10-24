package ch.deletescape.jterm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.deletescape.jterm.commandcontexts.Scripting;
import ch.deletescape.jterm.config.Resources;
import ch.deletescape.jterm.config.UserProperties;
import ch.deletescape.jterm.io.Printer;

public class CommandUtils {

  public static final Map<String, CommandExecutor> COMMAND_LISTENERS = new HashMap<String, CommandExecutor>();
  public static final Set<String> BASE_COMMANDS = new HashSet<>();
  private static final Pattern INLINE_COMMAND_PATTERN = Pattern.compile("\\$\\{.*?\\}");
  public static final Set<String> CONTEXTS = new HashSet<>();

  static void initializeEnv() throws IOException {
    try (InputStreamReader in = new InputStreamReader(CommandUtils.class.getResourceAsStream("/contexts.ctx"))) {
      BufferedReader br = new BufferedReader(in);
      String str;
      while ((str = br.readLine()) != null) {
        str = str.trim();
        initializeClass(str);
        CONTEXTS.add(str);
      }
    }
    BASE_COMMANDS.addAll(COMMAND_LISTENERS.keySet());
    Printer.out.mute(true);
    Path jtermrc = Paths.get(JTerm.getHome(), ".jtermrc");
    if (Files.exists(jtermrc)) {
      Scripting.run(jtermrc.toString());
    }
    Printer.out.mute(false);
    UserProperties.init();
  }

  public static Object evaluateCommand(String cmd) {
    Object ret = null;
    if (cmd.startsWith("#") || cmd.startsWith("rem ")) {
      return null;
    }
    try {
      int indx = cmd.indexOf(' ');
      String key = indx != -1 ? cmd.substring(0, indx) : cmd;
      CommandExecutor executor = COMMAND_LISTENERS.get(key);
      if (executor != null) {
        ret = executor.exec(getArgs(cmd));
      } else if (cmd.startsWith("./")) {
        ret = Scripting.run(cmd);
      } else {
        Printer.err.println(Resources.getString("CommandUtils.UnknownCommand"), key);
        Printer.out.println(Resources.getString("CommandUtils.UnknownCommandInfo"));
      }
    } catch (Exception e) {
      Printer.err.println(Resources.getString("CommandUtils.Error"), e.getMessage());
    }
    return ret;
  }

  public static void addListener(String cmd, CommandExecutor exec) {
    COMMAND_LISTENERS.put(cmd, exec);
  }

  static void printInputPromt() {
    String formattedPath = JTerm.getCurrPath().toString().replace(JTerm.getHome(), "~");
    Printer.out.forced().print("%s %s > ", JTerm.getUser(), formattedPath);
  }

  private static void initializeClass(String clazz) {
    try {
      Class.forName(clazz).newInstance();
    } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
      Printer.err.println(Resources.getString("CommandUtils.ErrorLoadingContext"), clazz, e);
    }
  }

  static String getArgs(String cmd) {
    int indx = cmd.indexOf(' ');
    return indx != -1 ? cmd.substring(indx + 1).trim() : "";
  }

  public static String parseInlineCommands(String tmp) {
    boolean wasMuted = Printer.out.mute(true);
    for (Matcher m = INLINE_COMMAND_PATTERN.matcher(tmp); m.find(); m = INLINE_COMMAND_PATTERN.matcher(tmp)) {
      String group = m.group();
      Object cmdResult = CommandUtils.evaluateCommand(group.replaceAll("^\\$\\{|\\}$", ""));
      tmp = tmp.substring(0, m.start()) + cmdResult + tmp.substring(m.end());
    }
    Printer.out.mute(wasMuted);
    return tmp;
  }
}
