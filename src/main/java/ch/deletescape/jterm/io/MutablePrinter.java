package ch.deletescape.jterm.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * A wrapper class for {@link PrintStream} to enable the stream to be muted
 * 
 * @author deletescape
 */
public class MutablePrinter {
  private final PrintStream stream;
  private boolean muted = false;
  private boolean forced = false;
  private static final PrintStream EMPTY_STREAM = new PrintStream(new OutputStream() {

    @Override
    public void write(int b) throws IOException {
      // Empty write method to avoid output if stream is requested but Printer is muted
    }
  });

  MutablePrinter(PrintStream stream) {
    this.stream = stream;
  }

  /**
   * Same as {@link #print(Object)} with an added linebreak at the end
   * 
   * @param x
   *          The object to be printed out
   * @return the printed string
   */
  public String println(Object x) {
    if (forced || !muted) {
      stream.println(x);
      forced = false;
    }
    return x != null ? x.toString() : null;
  }

  /**
   * Same as {@link #print(String, Object...)} with an added linebreak at the end
   * 
   * @param format
   *          A <a href="../util/Formatter.html#syntax">format string</a>
   * @param args
   *          Arguments referenced by the format specifiers in the format
   *          string. If there are more arguments than format specifiers, the
   *          extra arguments are ignored. The number of arguments is
   *          variable and may be zero. The maximum number of arguments is
   *          limited by the maximum dimension of a Java array as defined by
   *          <cite>The Java&trade; Virtual Machine Specification</cite>.
   *          The behaviour on a
   *          {@code null} argument depends on the <a
   *          href="../util/Formatter.html#syntax">conversion</a>.
   * @return the printed String
   */
  public String println(String format, Object... args) {
    return println(String.format(format, args));
  }

  /**
   * Prints a linebreak
   * 
   * @return The printed out string
   */
  public String println() {
    return println("");
  }

  /**
   * If this MutablePrinter isn't muted or output is enforced this prints an Object
   * 
   * @param x
   *          The Object to be printed
   * @return The printed out string
   */
  public String print(Object x) {
    if (forced || !muted) {
      stream.print(x);
      forced = false;
    }
    return x != null ? x.toString() : null;
  }

  /**
   * If this MutablePrinter isn't muted or output is enforced this prints a formatted string
   * 
   * @param format
   *          A <a href="../util/Formatter.html#syntax">format string</a>
   * @param args
   *          Arguments referenced by the format specifiers in the format
   *          string. If there are more arguments than format specifiers, the
   *          extra arguments are ignored. The number of arguments is
   *          variable and may be zero. The maximum number of arguments is
   *          limited by the maximum dimension of a Java array as defined by
   *          <cite>The Java&trade; Virtual Machine Specification</cite>.
   *          The behaviour on a
   *          {@code null} argument depends on the <a
   *          href="../util/Formatter.html#syntax">conversion</a>.
   * @return the printed String
   */
  public String print(String format, Object... args) {
    return print(String.format(format, args));
  }

  /**
   * Can be used to access the {@link PrintStream} of this MutablePrinter
   * 
   * @return If this MutablePrinter isn't muted or output is enforced the used {@link PrintStream}
   *         is returned, otherwise a muted PrintStream is returned
   */
  public PrintStream getPrintStream() {
    if (forced || !muted) {
      forced = false;
      return stream;
    }
    return EMPTY_STREAM;
  }

  /**
   * Mute / Unmute this MutablePrinter, a muted printer won't print unless called with
   * {@link #forced}
   * 
   * @param mute
   *          Wheter or not the printer should be muted
   * @return The previous state of this Printer
   */
  public boolean mute(boolean mute) {
    boolean currState = muted;
    muted = mute;
    return currState;
  }

  /**
   * Inverts the current mute state of this MutablePrinter
   * 
   * @return The new state
   */
  public boolean toggleMute() {
    muted = !muted;
    return muted;
  }
/**
 * Access the current mute state
 * @return The current mute state
 */
  public boolean isMuted() {
    return muted;
  }
/**
 * Enforce output on the next call to any method which writes to the stream, even if it is muted<br>
 * Should be used as chaining element: {@code mutablePrinter.forced().print("This output is enforced")}
 * @return This MutablePrinter to enable chaining
 */
  public MutablePrinter forced() {
    forced = true;
    return this;
  }
}
