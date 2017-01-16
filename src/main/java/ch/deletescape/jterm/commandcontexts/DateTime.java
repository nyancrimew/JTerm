package ch.deletescape.jterm.commandcontexts;

import java.text.DateFormat;
import java.util.Date;

import ch.deletescape.jterm.CommandUtils;
import ch.deletescape.jterm.io.Printer;

public class DateTime extends CommandContext {
  private static final DateFormat DATE_FORMAT = DateFormat.getDateInstance();
  private static final DateFormat TIME_FORMAT = DateFormat.getTimeInstance();

  @Override
  protected void init() {
    CommandUtils.addListener("time", o -> time());
    CommandUtils.addListener("date", o -> date());
  }

  private String time() {
    return Printer.out.println(TIME_FORMAT.format(new Date()));
  }

  private String date() {
    return Printer.out.println(DATE_FORMAT.format(new Date()));
  }
}
