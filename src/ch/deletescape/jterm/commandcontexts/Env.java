package ch.deletescape.jterm.commandcontexts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

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
    CommandUtils.addListener("run", Env::run);
    CommandUtils.addListener("os", this::os);
    CommandUtils.addListener("alias", this::alias);
    CommandUtils.addListener("mute", o -> mute());
  }

  private String getEnv(String cmd) {
    StringBuilder output = new StringBuilder();
    String env = cmd;
    if (!"".equals(env)) {
      output.append(System.getenv(env) + "\n");
    } else {
      System.getenv().forEach((s1, s2) -> output.append(s1 + "=" + s2 + "\n"));
    }
    Printer.out.println(output.toString());
    return output.toString();
  }

  private String exec(String cmd) throws IOException {
    Process proc = Runtime.getRuntime().exec(cmd);
    InputStream in = proc.getInputStream();
    return Util.copyStream(in, Printer.out.getPrintStream());
  }

  private Object exit() {
    Printer.out.println("Exiting");
    JTerm.exit();
    return null;
  }

  public static boolean run(String cmd) throws IOException {
    try {
      Path path = JTerm.getCurrPath().resolve(Util.makePathString(cmd)).toRealPath();
      if (Files.isDirectory(path)) {
        Printer.err.println(path + " is a directory!");
      } else {
        Path bak = JTerm.getCurrPath();
        JTerm.setCurrPath(path.getParent());
        try (BufferedReader in = Files.newBufferedReader(path)) {
          String s = in.readLine();
          while (s != null) {
            while (s.endsWith("\\;")) {
              s = s.substring(0, s.length() - 2) + " " + in.readLine().trim();
            }
            s = s.trim();
            if (!"".equals(s)) {
              CommandUtils.evaluateCommand(s);
            }
            s = in.readLine();
          }
        }
        JTerm.setCurrPath(bak);
        return true;
      }
    } catch (NoSuchFileException e) {
      Printer.out
          .println("Error: Path \"" + JTerm.getCurrPath().resolve(Util.makePathString(cmd)) + "\" couldn't be found!");
    }
    return false;
  }

  private String os(String arg) {
    String name = System.getProperty("os.name");
    String arch = System.getProperty("os.arch");
    String version = System.getProperty("os.version");
    StringBuilder sb = new StringBuilder();
    switch (arg) {
      case "":
        sb.append("Name . . . . . : ");
        sb.append(name);
        sb.append('\n');
        sb.append("Architecture . : ");
        sb.append(arch);
        sb.append('\n');
        sb.append("Version. . . . : ");
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
        String errTXT = "Unknown option: " + arg;
        Printer.err.println(errTXT);
      case "-h":
      case "--help":
        sb.append("Usage: os [-[nvah]]");
        sb.append("\n\t-n / --name . . . : Prints os name");
        sb.append("\n\t-a / --arch . . . : Prints os architecture");
        sb.append("\n\t-v / --version. . : Prints os version");
        sb.append("\n\t-h / --help . . . : Prints this usage information\n");
    }
    Printer.out.println(sb.toString());
    return sb.toString();
  }

  private String alias(String cmd) {
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
    CommandUtils.addListener(alias, (o) -> CommandUtils.evaluateCommand((original + " " + o).trim()));
    return successTxt;
  }

  private boolean mute() {
    return Printer.out.toggleMute();
  }

}
