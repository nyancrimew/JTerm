package ch.deletescape.jterm.commandcontexts;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import ch.deletescape.jterm.CommandUtils;
import ch.deletescape.jterm.io.Printer;

public class DateTime implements CommandContext {
  private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("YYYY-MM-dd");
  private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("hh:mm:ss.SSS");

  private String time() {
    Date time = Calendar.getInstance().getTime();
    String timeString = TIME_FORMAT.format(time);
    Printer.out.println(timeString);
    return timeString;
  }

  private String date() {
    Date date = Calendar.getInstance().getTime();
    String dateString = DATE_FORMAT.format(date);
    Printer.out.println(dateString);
    return dateString;
  }

  @Override
  public void init() {
    CommandUtils.addListener("time", (o) -> time());
    CommandUtils.addListener("date", (o) -> date());
  }
}
