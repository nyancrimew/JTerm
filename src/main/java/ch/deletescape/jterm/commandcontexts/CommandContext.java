package ch.deletescape.jterm.commandcontexts;

public abstract class CommandContext {
  public CommandContext() {
    init();
  }

  protected abstract void init();

}
