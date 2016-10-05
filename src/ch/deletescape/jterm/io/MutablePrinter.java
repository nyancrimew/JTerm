package ch.deletescape.jterm.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class MutablePrinter {
  private final PrintStream stream;
  private boolean muted = false;
  private static final PrintStream EMPTY_STREAM = new PrintStream(new OutputStream() {

    @Override
    public void write(int b) throws IOException {
    }
  });

  MutablePrinter(PrintStream stream) {
    this.stream = stream;
  }

  public void println(Object x) {
    if (!muted) {
      stream.println(x);
    }
  }

  public void println() {
    if (!muted) {
      stream.println();
    }
  }

  public void print(Object x) {
    if (!muted) {
      stream.print(x);
    }
  }

  public void mute(boolean mute) {
    muted = mute;
  }

  public boolean toggleMute() {
    muted = !muted;
    return muted;
  }

  public boolean isMuted() {
    return muted;
  }

  public PrintStream getPrintStream() {
    if (!muted) {
      return stream;
    }
    return EMPTY_STREAM;
  }
}
