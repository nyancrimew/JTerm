package ch.deletescape.jterm.commandcontexts;

import java.io.IOException;

import ch.deletescape.jterm.CommandUtils;
import ch.deletescape.jterm.JTerm;
import ch.deletescape.jterm.Util;
import ch.deletescape.jterm.io.Printer;

public class Env implements CommandContext {

  @Override
  public void init() {
    CommandUtils.addListener("getEnv", this::getEnv);
    CommandUtils.addListener("exec", this::exec);
    CommandUtils.addListener("exit", o -> exit());
    CommandUtils.addListener("bye", o -> exit());
    CommandUtils.addListener("os", this::os);
    CommandUtils.addListener("alias", this::alias);
    CommandUtils.addListener("mute", o -> mute());
  }

  private String getEnv(String cmd) {
    cmd = CommandUtils.parseInlineCommands(cmd);
    StringBuilder output = new StringBuilder();
    if (!"".equals(cmd)) {
      output.append(System.getenv(cmd) + "\n");
    } else {
      System.getenv().forEach((s1, s2) -> output.append(s1 + "=" + s2 + "\n"));
    }
    Printer.out.println(output.toString());
    return output.toString();
  }

  private String exec(String cmd) throws IOException {
    cmd = CommandUtils.parseInlineCommands(cmd);
    Process proc = Runtime.getRuntime().exec(cmd);
    return Util.copyStream(proc.getInputStream(), Printer.out.getPrintStream());
  }

  private Object exit() {
    JTerm.exit();
    return null;
  }

  private String os(String arg) {
    arg = CommandUtils.parseInlineCommands(arg);
    String name = System.getProperty("os.name");
    String arch = System.getProperty("os.arch");
    String version = System.getProperty("os.version");
    StringBuilder sb = new StringBuilder();
    switch (arg) {
      case "":
        sb.append("Name . . . . . : ");
        sb.append(name);
        sb.append("\nArchitecture . : ");
        sb.append(arch);
        sb.append("\nVersion. . . . : ");
        sb.append(version);
        sb.append('\n');
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
        Printer.err.println("Unknown option: " + arg);
      case "-h":
      case "--help":
        sb.append("Usage: os [-[nvah]]");
        sb.append("\n\t-n / --name . . . : Prints os name\n\t-a / --arch . . . : Prints os architecture");
        sb.append("\n\t-v / --version. . : Prints os version\n\t-h / --help . . . : Prints this usage information\n");
        break;
    }
    Printer.out.println(sb.toString());
    return sb.toString();
  }

  private String alias(String cmd) {
    cmd = CommandUtils.parseInlineCommands(cmd);
    String str = cmd.trim();
    String alias = str.split("=")[0].trim().replaceAll(" ", "_");
    String original = str.split("=")[1].trim();
    if (CommandUtils.COMMAND_LISTENERS.containsKey(alias)) {
      String errTxt = "Can't set alias \"" + alias + "\", a command with that name already exists!";
      Printer.err.println(errTxt);
      return errTxt;
    }
    String successTxt = "Setting alias \"" + alias + "\" for command \"" + original + "\"";
    Printer.out.println(successTxt);
    CommandUtils.addListener(alias, o -> CommandUtils.evaluateCommand((original + " " + o).trim()));
    return successTxt;
  }

  private boolean mute() {
    return Printer.out.toggleMute();
  }

}
