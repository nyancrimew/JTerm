package ch.deletescape.jterm.commandcontexts;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import ch.deletescape.jterm.CommandUtils;
import ch.deletescape.jterm.config.Resources;
import ch.deletescape.jterm.io.Printer;

public class DateTime extends CommandContext {
  private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(Resources.getString("DateTime.DateFormat")); 
  private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat(Resources.getString("DateTime.TimeFormat"));

  @Override
  public void init() {
    CommandUtils.addListener("time", o -> time()); 
    CommandUtils.addListener("date", o -> date()); 
  }

  private String time() {
    Date time = Calendar.getInstance().getTime();
    return Printer.out.println(TIME_FORMAT.format(time));
  }

  private String date() {
    Date date = Calendar.getInstance().getTime();
    return Printer.out.println(DATE_FORMAT.format(date));
  }
}
