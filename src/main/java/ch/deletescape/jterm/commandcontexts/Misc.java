package ch.deletescape.jterm.commandcontexts;

import javax.script.ScriptException;

import ch.deletescape.jterm.CommandUtils;
import ch.deletescape.jterm.JTerm;
import ch.deletescape.jterm.io.Printer;

public class Misc extends CommandContext {

  @Override
  public void init() {
    CommandUtils.addListener("calc", this::calc);
    CommandUtils.addListener("help", this::help);
    CommandUtils.addListener("?", this::help);
    CommandUtils.addListener("echo", this::echo);
    CommandUtils.addListener("echo.", this::echo);
    CommandUtils.addListener("print", this::echo);
  }

  Object calc(String cmd) throws ScriptException {
    String operation = CommandUtils.parseInlineCommands(cmd);
    Object result = JTerm.getJsEngine().eval(operation);
    Printer.out.println(result);
    return result;
  }

  private String help(String arg) {
    String argument = CommandUtils.parseInlineCommands(arg);
    StringBuilder output = new StringBuilder("Available commands:\n");
    if ("-a".equals(argument) || "-all".equals(argument)) {
      CommandUtils.COMMAND_LISTENERS.keySet().stream().sorted().forEach(s -> output.append("\t" + s + "\n"));
    } else {
      CommandUtils.BASE_COMMANDS.stream().sorted().forEach(s -> output.append("\t" + s + "\n"));
    }
    return Printer.out.println(output);
  }

  String echo(String cmd) {
    String text = CommandUtils.parseInlineCommands(cmd);
    return Printer.out.println(text.replaceAll("^\"|\"$", ""));
  }
}
