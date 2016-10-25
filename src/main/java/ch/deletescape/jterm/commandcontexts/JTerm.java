package ch.deletescape.jterm.commandcontexts;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ch.deletescape.jterm.CommandUtils;
import ch.deletescape.jterm.config.Resources;
import ch.deletescape.jterm.io.Printer;

public class JTerm extends CommandContext {
  private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyyMMddhhmmss");
  private static final DateFormat LOCALE_FORMAT = DateFormat.getDateTimeInstance();

  @Override
  protected void init() {
    CommandUtils.addListener("version", o -> version());
    CommandUtils.addListener("buildtime", o -> buildTime());
  }

  private String version() {
    return Printer.out.println(Resources.getString("JTerm.Version"));
  }

  private String buildTime() throws ParseException {
    Date date = FORMAT.parse(Resources.getString("JTerm.BuildTimestamp"));
    return Printer.out.println(LOCALE_FORMAT.format(date));
  }
}
