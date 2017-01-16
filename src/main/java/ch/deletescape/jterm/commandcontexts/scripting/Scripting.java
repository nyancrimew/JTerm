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

  boolean ifThenElse(String args) throws ScriptException {
    String[] thenSplit = args.split(" then ");
    String[] elseSplit = thenSplit[1].split(" else ");
    if (eval(thenSplit[0])) {
      CommandUtils.evaluateCommand(CommandUtils.parseInlineCommands(elseSplit[0]));
      return true;
    }
    if (elseSplit.length > 1) {
      CommandUtils.evaluateCommand(CommandUtils.parseInlineCommands(elseSplit[1]));
    }
    return false;
  }

  private int whileDo(String args) throws ScriptException {
    int count = 0;
    String[] doSplit = args.split(" do ");
    while (eval(doSplit[0])) {
      CommandUtils.evaluateCommand(CommandUtils.parseInlineCommands(doSplit[1]));
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
      return JTerm.getJsEngine().eval(name + " = " + value);
    }
    validateVariableName(arguments);
    Object value = JTerm.getJsEngine().eval(arguments);
    Printer.out.println(value);
    return value;
  }

  void validateVariableName(String name) throws ScriptingException {
    if (!name.matches("^[a-zA-Z_$][\\w$]*")) {
      throw new ScriptingException(String.format(Resources.getString("Scripting.InvalidVariableName"), name));
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
