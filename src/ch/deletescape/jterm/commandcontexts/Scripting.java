package ch.deletescape.jterm.commandcontexts;

import javax.script.ScriptException;

import ch.deletescape.jterm.CommandUtils;
import ch.deletescape.jterm.JTerm;
import ch.deletescape.jterm.io.Printer;

public class Scripting implements CommandContext {

  @Override
  public void init() {
    CommandUtils.addListener("if", this::ifThenElse);
    CommandUtils.addListener("while", this::whileDo);
    CommandUtils.addListener("readLine", this::readLine);
    CommandUtils.addListener("var", this::var);
    CommandUtils.addListener("getVar", this::getVar);
  }

  private boolean eval(String arg) throws ScriptException {
    arg = CommandUtils.parseInlineCommands(arg);
    boolean result = (boolean) JTerm.getJsEngine().eval(arg);
    return result;
  }

  private boolean ifThenElse(String args) throws ScriptException {
    boolean hasElse = args.contains(" else ");
    String condition = args.split(" then ")[0];
    String part2 = args.split(" then ")[1];
    String then = hasElse ? part2.split(" else ")[0] : part2;
    if (eval(condition)) {
      CommandUtils.evaluateCommand(CommandUtils.parseInlineCommands(then));
      return true;
    }
    if (hasElse) {
      String elseCmd = part2.split(" else ")[1];
      CommandUtils.evaluateCommand(CommandUtils.parseInlineCommands(elseCmd));
    }
    return false;
  }

  private int whileDo(String args) throws ScriptException {
    int count = 0;
    String condition = args.split(" do ")[0];
    String command = args.split(" do ")[1];
    while (eval(condition)) {
      CommandUtils.evaluateCommand(CommandUtils.parseInlineCommands(command));
      count++;
    }
    return count;
  }

  private String readLine(String arg) {
    arg = CommandUtils.parseInlineCommands(arg);
    Printer.out.forced().print(arg + " ");
    return JTerm.getScanner().nextLine();
  }

  private Object var(String args) throws ScriptException {
    args = CommandUtils.parseInlineCommands(args);
    String name = args.split("=")[0].trim();
    String value = args.split("=")[1].trim();
    if (!value.matches("[0-9]*|true|false")) {
      value = "\"" + value + "\"";
    }
    JTerm.getJsEngine().eval("var " + name + " = " + value);
    return JTerm.getJsEngine().eval(name);
  }

  private Object getVar(String arg) throws ScriptException {
    arg = CommandUtils.parseInlineCommands(arg);
    Object value = JTerm.getJsEngine().eval(arg);
    Printer.out.println(value);
    return value;
  }

}
