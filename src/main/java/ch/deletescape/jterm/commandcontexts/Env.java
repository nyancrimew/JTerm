package ch.deletescape.jterm.commandcontexts;

import java.io.IOException;

import ch.deletescape.jterm.CommandUtils;
import ch.deletescape.jterm.JTerm;
import ch.deletescape.jterm.Util;
import ch.deletescape.jterm.config.Resources;
import ch.deletescape.jterm.config.UserProperties;
import ch.deletescape.jterm.io.Printer;

public class Env extends CommandContext {

  @Override
  public void init() {
    CommandUtils.addListener("getEnv", this::getEnv);
    CommandUtils.addListener("exec", this::exec);
    CommandUtils.addListener("exit", o -> exit());
    CommandUtils.addListener("bye", o -> exit());
    CommandUtils.addListener("os", this::os);
    CommandUtils.addListener("alias", this::alias);
    CommandUtils.addListener("mute", o -> mute());
    CommandUtils.addListener("setProp", this::setProp);
    CommandUtils.addListener("getProp", this::getProp);
  }

  private String getEnv(String cmd) {
    String command = CommandUtils.parseInlineCommands(cmd);
    StringBuilder output = new StringBuilder();
    if (!command.isEmpty()) {
      output.append(System.getenv(command) + "\n");
    } else {
      System.getenv().forEach((s1, s2) -> output.append(s1 + "=" + s2 + "\n"));
    }
    return Printer.out.println(output);
  }

  private String exec(String cmd) throws IOException {
    String command = CommandUtils.parseInlineCommands(cmd);
    Process proc = Runtime.getRuntime().exec(command);
    return Util.copyStream(proc.getInputStream(), Printer.out.getPrintStream());
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
    StringBuilder sb = new StringBuilder();
    switch (argument) {
      case "":
        sb.append(String.format(Resources.getString("Env.DefaultFormat"), name, arch, version));
        break;
      case "-n":
      case "--name":
        sb.append(name);
        break;
      case "-v":
      case "--version":
        sb.append(version);
        break;
      case "-a":
      case "--arch":
        sb.append(arch);
        break;
      default:
        Printer.err.println(Resources.getString("Env.UnknownOption"), argument);
      case "-h":
      case "--help":
        sb.append(Resources.getString("Env.UsageInfo"));
        break;
    }
    return Printer.out.println(sb);
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
    return Printer.out.println(UserProperties.getProperty(key));
  }

  private boolean mute() {
    return Printer.out.toggleMute();
  }

}
