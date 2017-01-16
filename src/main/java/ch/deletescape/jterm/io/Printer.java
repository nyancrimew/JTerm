package ch.deletescape.jterm.io;

/**
 * Convenience class to statically access two default {@link MutablePrinter}
 * 
 * @author deletescape
 */
public final class Printer {
  private Printer() {}

  /**
   * {@link MutablePrinter} printing to {@link System#out}
   */
  public static final MutablePrinter out = new MutablePrinter(System.out);
  /**
   * {@link MutablePrinter} printing to {@link System#err}
   */
  public static final MutablePrinter err = new MutablePrinter(System.err);
}
