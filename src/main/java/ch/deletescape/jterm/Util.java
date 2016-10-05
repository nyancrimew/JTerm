package ch.deletescape.jterm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
}
