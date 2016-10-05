package ch.deletescape.jterm.commandcontexts;

import javax.script.ScriptException;

import ch.deletescape.jterm.CommandUtils;
import ch.deletescape.jterm.JTerm;
import ch.deletescape.jterm.io.Printer;

public class Misc implements CommandContext {

  @Override
  public void init() {
    CommandUtils.addListener("calc", this::calc);
    CommandUtils.addListener("help", this::help);
    CommandUtils.addListener("?", this::help);
    CommandUtils.addListener("echo", this::echo);
    CommandUtils.addListener("echo.", this::echo);
    CommandUtils.addListener("print", this::echo);
  }

  private Object calc(String operation) throws ScriptException {
    Object result = JTerm.getJsEngine().eval(operation);
    Printer.out.println(result);
    return result;
  }

  private String help(String arg) {
    StringBuilder output = new StringBuilder("Available commands:\n");
    if ("-a".equals(arg) || "-all".equals(arg)) {
      CommandUtils.COMMAND_LISTENERS.keySet().stream().sorted().forEach(s -> output.append("\t" + s + "\n"));
    } else {
      CommandUtils.BASE_COMMANDS.stream().sorted().forEach(s -> output.append("\t" + s + "\n"));
    }
    Printer.out.println(output.toString());
    return output.toString();
  }

  private String echo(String text) {
    String output = text.trim().replaceAll("\"", "");
    Printer.out.println(output);
    return output;
  }
}
