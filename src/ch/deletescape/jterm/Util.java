package ch.deletescape.jterm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

public class Util {
  public static String makePathString(String cmd) {
    return cmd.replace("~", JTerm.getHome());
  }

  public static String copyStream(InputStream input, OutputStream output) throws IOException {
    StringBuilder sb = new StringBuilder();
    byte[] buffer = new byte[1024];
    int bytesRead;
    while ((bytesRead = input.read(buffer)) != -1) {
      output.write(buffer, 0, bytesRead);
      sb.append(new String(buffer, 0, bytesRead));
    }
    return sb.toString();
  }

  public static void muteSysOut() {
    System.setOut(new PrintStream(new OutputStream() {

      @Override
      public void write(int b) throws IOException {
      }
    }));
  }
}
