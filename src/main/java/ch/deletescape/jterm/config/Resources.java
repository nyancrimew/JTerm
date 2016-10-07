package ch.deletescape.jterm.config;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Resources {
  private static final ResourceBundle.Control CONTROL = new ResourceBundle.Control() {
    @Override
    public Locale getFallbackLocale(String baseName, Locale locale) {
      return Locale.ROOT;
    }
  };
  private static ResourceBundle strings = ResourceBundle.getBundle("strings", CONTROL);
  private static ResourceBundle help = ResourceBundle.getBundle("help", CONTROL);

  private Resources() {
  }

  public static String getString(String key) {
    try {
      return strings.getString(key);
    } catch (MissingResourceException e) {
      return '!' + key + '!';
    }
  }

  public static String getShortHelp(String command) {
    try {
      return help.getString(command + ".short");
    } catch (MissingResourceException e) {
      return "";
    }
  }

  public static String getHelp(String command) {
    try {
      return help.getString(command);
    } catch (MissingResourceException e) {
      return getShortHelp(command);
    }
  }

  static void localeChanged() {
    strings = ResourceBundle.getBundle("strings", CONTROL);
    help = ResourceBundle.getBundle("help", CONTROL);
  }
}
