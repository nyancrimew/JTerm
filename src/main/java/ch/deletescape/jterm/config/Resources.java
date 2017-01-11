package ch.deletescape.jterm.config;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class Resources {
  private static final Logger LOGGER = LogManager.getLogger();
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
      LOGGER.error(e.toString(), e);
      return '!' + key + '!';
    }
  }

  public static String getHelp(String command, boolean shortString) {
    if (!shortString) {
      try {
        return help.getString(command);
      } catch (MissingResourceException e) {
        LOGGER.error(e.toString()+", We will try to find the short help string in the next step", e);
      }
    }
    try {
      return help.getString(command + ".short");
    } catch (MissingResourceException e) {
      LOGGER.error(e.toString(), e);
      return "";
    }
  }

  static void localeChanged() {
    strings = ResourceBundle.getBundle("strings", CONTROL);
    help = ResourceBundle.getBundle("help", CONTROL);
  }
}
