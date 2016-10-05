package ch.deletescape.jterm.commandcontexts;

import javax.script.ScriptException;

import ch.deletescape.jterm.CommandUtils;
import ch.deletescape.jterm.JTerm;

public class Misc implements CommandContext {

  private Object calc(String operation) throws ScriptException {
    Object result = JTerm.getJsEngine().eval(operation);
    System.out.println(result);
    return result;
  }

  private String help(String arg) {
    StringBuilder output = new StringBuilder("Available commands:\n");
    if ("-a".equals(arg) || "-all".equals(arg)) {
      CommandUtils.COMMAND_LISTENERS.keySet().stream().sorted().forEach(s -> output.append("\t" + s + "\n"));
    } else {
      CommandUtils.BASE_COMMANDS.stream().sorted().forEach(s -> output.append("\t" + s + "\n"));
    }
    System.out.println(output.toString());
    return output.toString();
  }

  private String echo(String text) {
    String output = text.trim().replaceAll("\"", "");
    System.out.println(output);
    return output;
  }

  @Override
  public void init() {
    CommandUtils.addListener("calc", (o) -> calc(o));
    CommandUtils.addListener("help", (o) -> help(o));
    CommandUtils.addListener("?", (o) -> help(o));
    CommandUtils.addListener("echo", (o) -> echo(o));
    CommandUtils.addListener("echo.", (o) -> echo(o));
    CommandUtils.addListener("print", (o) -> echo(o));

  }
}
