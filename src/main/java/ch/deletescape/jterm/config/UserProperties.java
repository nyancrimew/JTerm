package ch.deletescape.jterm.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Properties;

import ch.deletescape.jterm.JTerm;
import ch.deletescape.jterm.io.Printer;

public class UserProperties {
  private static final Properties USER_PROPS = new Properties();
  private static final Path PROPERTIES_PATH = Paths.get(JTerm.getHome(), ".jterm/user.properties");

  private UserProperties() {
  }

  public static void init() {
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

  private static void loadProps() {
    if (Files.exists(PROPERTIES_PATH)) {
      try {
        USER_PROPS.load(Files.newInputStream(PROPERTIES_PATH));
      } catch (IOException e) {
        Printer.err.println(e.toString());
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
    }

  }

  private static void setDefaultLocale() {
    String userLocale = USER_PROPS.getProperty("user.locale");
    if (userLocale != null) {
      Locale.setDefault(Locale.forLanguageTag(userLocale));
    }
    Resources.localeChanged();
  }
}
