package ch.deletescape.jterm.commandcontexts;

import java.io.IOException;

import ch.deletescape.jterm.CommandUtils;
import ch.deletescape.jterm.JTerm;
import ch.deletescape.jterm.Util;
import ch.deletescape.jterm.config.Resources;
import ch.deletescape.jterm.config.UserProperties;
import ch.deletescape.jterm.io.Printer;

public class Env extends CommandContext {
  private String prefix;
  private String path;

  @Override
  protected void init() {
    CommandUtils.addListener("getEnv", this::getEnv);
    CommandUtils.addListener("exit", o -> exit());
    CommandUtils.addListener("os", this::os);
    CommandUtils.addListener("alias", this::alias);
    CommandUtils.addListener("mute", o -> mute());
    CommandUtils.addListener("setProp", this::setProp);
    CommandUtils.addListener("getProp", this::getProp);
    CommandUtils.addListener("exec", this::exec);
  }

  private String getEnv(String cmd) {
    String command = CommandUtils.parseInlineCommands(cmd);
    StringBuilder output = new StringBuilder();
    if (!command.isEmpty()) {
      output.append(System.getenv(command));
      output.append('\n');
    } else {
      System.getenv().forEach((s1, s2) -> output.append(s1 + "=" + s2 + "\n"));
    }
    return Printer.out.println(output);
  }

  private Object exit() {
    JTerm.exit();
    return null;
  }

  private String os(String arg) {
    String argument = CommandUtils.parseInlineCommands(arg);
    String name = System.getProperty("os.name");
    String arch = System.getProperty("os.arch");
    String version = System.getProperty("os.version");
    switch (argument) {
      case "":
        return Printer.out.println(Resources.getString("Env.DefaultFormat"), name, arch, version);
      case "-n":
      case "--name":
        return Printer.out.println(name);
      case "-v":
      case "--version":
        return Printer.out.println(version);
      case "-a":
      case "--arch":
        return Printer.out.println(arch);
      default:
        Printer.err.println(Resources.getString("Env.UnknownOption"), argument);
        return "";
    }
  }

  String alias(String cmd) {
    String command = CommandUtils.parseInlineCommands(cmd);
    String alias = command.split("=")[0].trim().replaceAll(" ", "_");
    String original = command.split("=")[1].trim();
    if (CommandUtils.COMMAND_LISTENERS.containsKey(alias)) {
      return Printer.err.println(Resources.getString("Env.CantSetAlias"), alias);
    }
    CommandUtils.addListener(alias, o -> CommandUtils.evaluateCommand((original + " " + o).trim()));
    return Printer.out.println(Resources.getString("Env.SettingAlias"), alias, original);
  }

  String setProp(String cmd) {
    String command = CommandUtils.parseInlineCommands(cmd);
    String key = command.split("=")[0].trim();
    String value = command.split("=")[1].trim();
    UserProperties.setProperty(key, value);
    return Printer.out.println(Resources.getString("Env.SettingProp"), key, value);
  }

  String getProp(String cmd) {
    String key = CommandUtils.parseInlineCommands(cmd);
    if (!key.isEmpty()) {
      return Printer.out.println(UserProperties.getProperty(key));
    }
    StringBuilder sb = new StringBuilder();
    UserProperties.getProperties().forEach((o1, o2) -> sb.append(o1 + " = " + o2 + "\n"));
    return Printer.out.println(sb.toString());
  }

  String exec(String cmd) throws IOException {
    String[] path = { getPath() };
    String command = getPrefix() + CommandUtils.parseInlineCommands(cmd);
    Process p = Runtime.getRuntime().exec(command, path, JTerm.getCurrPath().toFile());
    return Util.copyStream(p.getInputStream(), Printer.out.getPrintStream());
  }

  private String getPrefix() {
    if (prefix == null) {
      boolean isWindows = System.getProperty("os.name").startsWith("Windows");
      prefix = isWindows ? "cmd /c set PATH=" + getPath() + "&&" : "";
    }
    return prefix;
  }

  private String getPath() {
    if (path == null) {
      path = System.getenv("PATH");
    }
    return path;
  }

  boolean mute() {
    return Printer.out.toggleMute();
  }

}
