package ch.deletescape.jterm;

/**
 * FunctionalInterface used for the action of a command
 * 
 * @author deletescape
 */
@FunctionalInterface
public interface CommandExecutor {
  /**
   * This method should contain the logic of a command, or a call to this logic
   * 
   * @param args
   *          The arguments for the command
   * @return The return value of the command
   * @throws Exception
   *           To enable exception throwing from commands
   */
  Object exec(String args) throws Exception;
}
