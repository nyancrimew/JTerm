package ch.deletescape.jterm.commandcontexts;

import ch.deletescape.jterm.CommandUtils;
import ch.deletescape.jterm.config.Resources;
import ch.deletescape.jterm.io.Printer;
import net.objecthunter.exp4j.ExpressionBuilder;

public class Misc extends CommandContext {

  @Override
  protected void init() {
    CommandUtils.addListener("calc", this::calc);
    CommandUtils.addListener("help", this::help);
    CommandUtils.addListener("echo", this::echo);
  }

  double calc(String cmd) {
    String operation = CommandUtils.parseInlineCommands(cmd);
    double result = new ExpressionBuilder(operation).build().evaluate();
    String out = ((int) result == result) ? String.valueOf((int) result) : String.valueOf(result);
    Printer.out.println(out);
    return result;
  }

  private String help(String arg) {
    String command = CommandUtils.parseInlineCommands(arg);
    StringBuilder output = new StringBuilder();
    if (arg.isEmpty()) {
      output.append(Resources.getString("Misc.HelpTitle"));
      CommandUtils.BASE_COMMANDS.stream().sorted().forEach(s -> appendCommand(output, s));
    } else {
      String help = Resources.getHelp(command, false);
      if (help.isEmpty()) {
        help = String.format(Resources.getString("Misc.NoHelpFound"), command);
      }
      output.append(help);
    }
    return Printer.out.println(output);
  }

  private void appendCommand(StringBuilder sb, String command) {
    sb.append(command);
    for(int i=15-command.length();i>0;i--){
      sb.append(' ');
    }
    sb.append(Resources.getHelp(command, true));
    sb.append('\n');
  }

  String echo(String cmd) {
    String text = CommandUtils.parseInlineCommands(cmd);
    return Printer.out.println(text.replaceAll("^\"|\"$", ""));
  }
}
