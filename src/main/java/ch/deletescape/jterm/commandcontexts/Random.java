package ch.deletescape.jterm.commandcontexts;

import ch.deletescape.jterm.CommandUtils;
import ch.deletescape.jterm.io.Printer;

public class Random extends CommandContext {
  private static long seedUniquifier = 8682522807148012L;

  @Override
  public void init() {
    CommandUtils.addListener("random", o -> random());
  }

  private long random() {
    long x = System.currentTimeMillis() * ++seedUniquifier;
    x ^= (x << 21);
    x ^= (x >>> 35);
    x ^= (x << 4);
    Printer.out.println(Math.abs(x));
    return Math.abs(x);
  }
}
