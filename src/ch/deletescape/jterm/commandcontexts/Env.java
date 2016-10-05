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

public class Env implements CommandContext {
  private boolean jTerm(String arg) throws IOException {
    JTerm.main(arg.length() > 0 ? arg.split(" ") : new String[0]);
    return true;
  }

  private String getEnv(String cmd) {
    StringBuilder output = new StringBuilder();
    String env = cmd;
    if (!"".equals(env)) {
      output.append(System.getenv(env) + "\n");
    } else {
      System.getenv().forEach((s1, s2) -> output.append(s1 + "=" + s2 + "\n"));
    }
    System.out.println(output.toString());
    return output.toString();
  }

  private String exec(String cmd) throws IOException {
    Process proc = Runtime.getRuntime().exec(cmd);
    InputStream in = proc.getInputStream();
    return Util.copyStream(in, System.out);
  }

  private Object exit() {
    System.out.println("Exiting");
    JTerm.exit();
    return null;
  }

  public static boolean run(String cmd) throws IOException {
    try {
      Path path = JTerm.getCurrPath().resolve(Util.makePathString(cmd)).toRealPath();
      if (Files.isDirectory(path)) {
        System.err.println(path + " is a directory!");
      } else {
        Path bak = JTerm.getCurrPath();
        JTerm.setCurrPath(path.getParent());
        try (BufferedReader in = Files.newBufferedReader(path)) {
          in.lines().forEach(CommandUtils::evaluateCommand);
        }
        JTerm.setCurrPath(bak);
        return true;
      }
    } catch (NoSuchFileException e) {
      System.out
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
        System.err.println(errTXT);
      case "-h":
      case "--help":
        sb.append("Usage: os [-[nvah]]");
        sb.append("\n\t-n / --name . . . : Prints os name");
        sb.append("\n\t-a / --arch . . . : Prints os architecture");
        sb.append("\n\t-v / --version. . : Prints os version");
        sb.append("\n\t-h / --help . . . : Prints this usage information\n");
    }
    System.out.println(sb.toString());
    return sb.toString();
  }

  private String alias(String cmd) {
    String str = cmd.trim();
    String alias = str.split("=")[0].trim().replaceAll(" ", "_");
    String original = str.split("=")[1].trim();
    if (CommandUtils.COMMAND_LISTENERS.containsKey(alias)) {
      String errTxt = "Can't set alias \"" + alias + "\", a command with that name already exists!";
      System.err.println(errTxt);
      return errTxt;
    }
    String successTxt = "Setting alias \"" + alias + "\" for command \"" + original + "\"";
    System.out.println(successTxt);
    CommandUtils.addListener(alias, (o) -> CommandUtils.evaluateCommand((original + " " + o).trim()));
    return successTxt;
  }

  @Override
  public void init() {
    CommandUtils.addListener("getEnv", (o) -> getEnv(o));
    CommandUtils.addListener("exec", (o) -> exec(o));
    CommandUtils.addListener("exit", (o) -> exit());
    CommandUtils.addListener("bye", (o) -> exit());
    CommandUtils.addListener("run", (o) -> run(o));
    CommandUtils.addListener("os", (o) -> os(o));
    CommandUtils.addListener("alias", o -> alias(o));
    CommandUtils.addListener("jTerm", o -> jTerm(o));
  }

}
