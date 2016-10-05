package ch.deletescape.jterm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.deletescape.jterm.commandcontexts.CommandContext;
import ch.deletescape.jterm.commandcontexts.Env;
import ch.deletescape.jterm.io.Printer;

public class CommandUtils {

  public static final Map<String, CommandExecutor> COMMAND_LISTENERS = new HashMap<String, CommandExecutor>();
  public static final Set<String> BASE_COMMANDS = new HashSet<>();
  private static final Pattern INLINE_COMMAND_PATTERN = Pattern.compile("\\$\\{.*?\\}");

  static void initializeEnv() throws IOException {
    try (InputStreamReader in = new InputStreamReader(CommandUtils.class.getResourceAsStream("/contexts.ctx"))) {
      BufferedReader br = new BufferedReader(in);
      for (String str = br.readLine(); str != null; str = br.readLine()) {
        str = str.trim();
        invokeInit(str);
      }
    }
    BASE_COMMANDS.addAll(COMMAND_LISTENERS.keySet());
    Printer.out.mute(true);
    Path jtermrc = JTerm.getCurrPath().resolve(".jtermrc");
    if (Files.exists(jtermrc)) {
      Env.run(jtermrc.toString());
    }
    Printer.out.mute(false);
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
        ret = Env.run(cmd);
      } else {
        Printer.err.println("Unknown command: " + key);
        Printer.out.println("To get a list of available commands enter \"help\"");
      }
    } catch (Exception e) {
      Printer.err.println("Error: " + e.getMessage());
    }
    return ret;
  }

  public static void addListener(String cmd, CommandExecutor exec) {
    COMMAND_LISTENERS.put(cmd, exec);
  }

  static void printInputPromt() {
    Printer.out.print(JTerm.getUsr() + " " + JTerm.getCurrPath().toString().replace(JTerm.getHome(), "~") + " > ");
  }

  private static void invokeInit(String clazz) {
    try {
      ((CommandContext) Class.forName(clazz).newInstance()).init();
    } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
      Printer.err.println("Error while trying to load context \"" + clazz + "\" : " + e.toString());
    }
  }

  private static String getArgs(String cmd) {
    int indx = cmd.indexOf(' ');
    String tmp = indx != -1 ? cmd.substring(indx + 1) : "";
    for (Matcher m = INLINE_COMMAND_PATTERN.matcher(tmp); m.find(); m = INLINE_COMMAND_PATTERN.matcher(tmp)) {

      String group = m.group();
      boolean isReadLine = group.contains("readLine");
      if (!isReadLine) {
        Printer.out.mute(true);
      }
      Object cmdResult = CommandUtils.evaluateCommand(group.replaceAll("^\\$\\{|\\}$", ""));
      tmp = tmp.substring(0, m.start()) + cmdResult + tmp.substring(m.end());
      Printer.out.mute(false);
    }
    return tmp;
  }
}
