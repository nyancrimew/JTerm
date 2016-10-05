package ch.deletescape.jterm.io;

public class Printer {
  public static final MutablePrinter out = new MutablePrinter(System.out);
  public static final MutablePrinter err = new MutablePrinter(System.err);
}
