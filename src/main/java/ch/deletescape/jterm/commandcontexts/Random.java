package ch.deletescape.jterm.commandcontexts;

import ch.deletescape.jterm.CommandUtils;

public class Random extends CommandContext {
  private java.util.Random rand;

  @Override
  protected void init() {
    CommandUtils.addListener("random", o -> random());
  }

  private long random() {
    if (rand == null) {
      rand = new java.util.Random();
    }
    return rand.nextLong();
  }
}
