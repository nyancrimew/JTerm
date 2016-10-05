package ch.deletescape.jterm.commandcontexts;

public abstract class CommandContext {
  public CommandContext() {
    init();
  }

  public abstract void init();

}
