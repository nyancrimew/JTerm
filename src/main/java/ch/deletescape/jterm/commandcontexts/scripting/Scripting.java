package ch.deletescape.jterm.commandcontexts.scripting;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.script.ScriptException;

import ch.deletescape.jterm.CommandUtils;
import ch.deletescape.jterm.JTerm;
import ch.deletescape.jterm.Util;
import ch.deletescape.jterm.commandcontexts.CommandContext;
import ch.deletescape.jterm.config.Resources;
import ch.deletescape.jterm.io.Printer;

public class Scripting extends CommandContext {

  @Override
  protected void init() {
    CommandUtils.addListener("if", this::ifThenElse);
    CommandUtils.addListener("while", this::whileDo);
    CommandUtils.addListener("readLine", this::readLine);
    CommandUtils.addListener("var", this::var);
    CommandUtils.addListener("run", Scripting::run);
  }

  boolean eval(String arg) throws ScriptException {
    String expr = CommandUtils.parseInlineCommands(arg);
    return (boolean) JTerm.getJsEngine().eval(expr);
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
    String hint = CommandUtils.parseInlineCommands(arg);
    Printer.out.forced().print(hint + " ");
    return JTerm.getScanner().nextLine();
  }

  Object var(String args) throws ScriptingException, ScriptException {
    String arguments = CommandUtils.parseInlineCommands(args);
    if (arguments.contains("=")) {
      String name = arguments.split("=")[0].trim();
      String value = arguments.split("=")[1].trim();
      validateVariableName(name);
      if (!value.matches("[0-9]*|true|false")) {
        value = "\"" + value + "\"";
      }
      JTerm.getJsEngine().eval("var " + name + " = " + value);
      return JTerm.getJsEngine().eval(name);
    }
    validateVariableName(arguments);
    Object value = JTerm.getJsEngine().eval(arguments);
    Printer.out.println(value);
    return value;
  }

  void validateVariableName(String name) throws ScriptingException {
    if(!name.matches("^[a-zA-Z_$][\\w$]*")){
      throw new ScriptingException(String.format(Resources.getString("Scripting.InvalidVariableName"),name));
    }
  }

  public static boolean run(String cmd) throws IOException {
    String command = CommandUtils.parseInlineCommands(cmd);
    Path path = JTerm.getCurrPath().resolve(Util.makePathString(command)).toRealPath();
    if (Files.isDirectory(path)) {
      Printer.err.println(Resources.getString("Scripting.FileIsDirectory"), path);
      return false;
    }
    Path bak = JTerm.getCurrPath();
    JTerm.setCurrPath(path.getParent());
    try (BufferedReader in = Files.newBufferedReader(path)) {
      for (String s = in.readLine(); s != null; s = in.readLine()) {
        while (s.endsWith("\\;")) {
          s = s.substring(0, s.length() - 2) + " " + in.readLine().trim();
        }
        s = s.trim();
        if (!s.isEmpty()) {
          CommandUtils.evaluateCommand(s);
        }
      }
    }
    JTerm.setCurrPath(bak);
    return true;
  }
}