package ch.deletescape.jterm.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Locale;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.deletescape.jterm.JTerm;
import ch.deletescape.jterm.io.Printer;

public class UserProperties {
  private static final Logger LOGGER = LogManager.getLogger();
  private static final Properties USER_PROPS = new Properties();
  private static final Path JTERM_DIR = Paths.get(JTerm.getHome(), ".jterm");
  private static final Path PROPERTIES_PATH = JTERM_DIR.resolve("user.properties");
  private static boolean firstStart;

  private UserProperties() {
  }

  public static void init() {
    firstStart();
    loadProps();
    setDefaultLocale();
  }

  public static void setProperty(String key, String value) {
    USER_PROPS.setProperty(key, value);
    saveProps();
  }

  public static String getProperty(String key, String defaultValue) {
    return USER_PROPS.getProperty(key, defaultValue);
  }

  public static String getProperty(String key) {
    return USER_PROPS.getProperty(key);
  }

  public static boolean getBoolean(String key) {
    String tmp = USER_PROPS.getProperty(key);
    return tmp!=null && ("true".equals(tmp) || "1".equals("tmp"));
  }

  public static boolean isFirstStart() {
    return firstStart;
  }

  private static void firstStart() {
    if (!Files.exists(JTERM_DIR)) {
      firstStart = true;
      try {
        Files.createDirectory(JTERM_DIR);
      } catch (IOException e) {
        Printer.err.println(e.toString());
        LOGGER.error(e.toString(), e);
      }
      setProperty("jterm.firststart", String.valueOf(Calendar.getInstance().getTimeInMillis()));
    }
  }

  private static void loadProps() {
    if (Files.exists(PROPERTIES_PATH)) {
      try {
        USER_PROPS.load(Files.newInputStream(PROPERTIES_PATH));
      } catch (IOException e) {
        Printer.err.println(e.toString());
        LOGGER.error(e.toString(), e);
      }
    }
  }

  private static void saveProps() {
    try {
      if (!Files.exists(PROPERTIES_PATH)) {
        Files.createFile(PROPERTIES_PATH);
      }
      USER_PROPS.store(Files.newOutputStream(PROPERTIES_PATH), "JTerm user properties");
    } catch (IOException e) {
      Printer.err.println(e.toString());
      LOGGER.error(e.toString(), e);
    }

  }

  private static void setDefaultLocale() {
    String userLocale = USER_PROPS.getProperty("user.locale");
    if (userLocale != null) {
      if (userLocale.contains("_")) {
        String[] localeParts = userLocale.split("_");
        Locale.setDefault(new Locale(localeParts[0], localeParts[1]));
      } else {
        Locale.setDefault(Locale.forLanguageTag(userLocale));
      }
    }
    Resources.localeChanged();
  }
}
