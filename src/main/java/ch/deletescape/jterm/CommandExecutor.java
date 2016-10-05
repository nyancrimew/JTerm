package ch.deletescape.jterm;

@FunctionalInterface
public interface CommandExecutor {
    Object exec(String args) throws Exception;
}
