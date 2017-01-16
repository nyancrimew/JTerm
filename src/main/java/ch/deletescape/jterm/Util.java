package ch.deletescape.jterm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class Util {

  private Util() {}

  /**
   * Replaces the tilde (~) with the home directory in a string
   * 
   * @param cmd
   *          The path to parse
   * @return The new String
   */
  public static String makePathString(String cmd) {
    return cmd.replace("~", JTerm.getHome());
  }

  /**
   * Writes the content of input into output
   * 
   * @param input
   *          The {@link InputStream} to be copied to
   * @param output
   *          The target {@link OutputStream} to copy to
   * @return The content of input
   * @throws IOException
   *           If IOException is thrown
   */
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
