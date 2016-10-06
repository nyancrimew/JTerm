package ch.deletescape.jterm;

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
  private static final ResourceBundle STRINGS = ResourceBundle.getBundle("strings", CONTROL);
  private static final ResourceBundle HELP = ResourceBundle.getBundle("help", CONTROL);

  private Resources() {
  }

  public static String getString(String key) {
    try {
      return STRINGS.getString(key);
    } catch (MissingResourceException e) {
      return '!' + key + '!';
    }
  }

  public static String getShortHelp(String command) {
    try {
      return HELP.getString(command + ".short");
    } catch (MissingResourceException e) {
      return "";
    }
  }

  public static String getHelp(String command) {
    try {
      return HELP.getString(command );
    } catch (MissingResourceException e) {
      return getShortHelp(command);
    }
  }
}
