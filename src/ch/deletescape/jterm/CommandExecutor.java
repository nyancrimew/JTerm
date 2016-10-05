package ch.deletescape.jterm;

@FunctionalInterface
public interface CommandExecutor {
  public Object exec(String args) throws Exception;
}
