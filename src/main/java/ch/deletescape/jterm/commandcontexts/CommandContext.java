package ch.deletescape.jterm.commandcontexts;

import ch.deletescape.jterm.CommandUtils;

/**
 * Abstract class to be extended by command contexts to provide the required api
 * 
 * @author deletescape
 */
public abstract class CommandContext {
  /**
   * Public constructor, calls {@link #init()} to initialize the context
   */
  public CommandContext() {
    init();
  }

  /**
   * Initialization method, should be used to register commands by calls to
   * {@link CommandUtils#addListener(String, ch.deletescape.jterm.CommandExecutor)}
   */
  protected abstract void init();

}
