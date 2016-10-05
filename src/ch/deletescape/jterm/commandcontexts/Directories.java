package ch.deletescape.jterm.commandcontexts;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.util.stream.Stream;

import ch.deletescape.jterm.CommandUtils;
import ch.deletescape.jterm.JTerm;
import ch.deletescape.jterm.Util;
import ch.deletescape.jterm.io.Printer;

public class Directories implements CommandContext {

  @Override
  public void init() {
    CommandUtils.addListener("cd", this::cd);
    CommandUtils.addListener("pwd", o -> pwd());
    CommandUtils.addListener("ls", this::ls);
    CommandUtils.addListener("dir", this::ls);
    CommandUtils.addListener("mkdir", this::mkdir);
  }

  private String pwd() {
    return Printer.out.println(JTerm.getCurrPath());
  }

  private boolean cd(String cmd) throws IOException {
    cmd = CommandUtils.parseInlineCommands(cmd);
    String path = Util.makePathString(cmd);
    try {
      Path temp = JTerm.getCurrPath().resolve(path).toRealPath();
      if (Files.isDirectory(temp)) {
        JTerm.setCurrPath(temp);
        pwd();
        return true;
      }
      Printer.out.println(temp + " is not a directory");
    } catch (NoSuchFileException e) {
      Printer.err.println("Error: Path \"" + JTerm.getCurrPath().resolve(path) + "\" couldn't be found!");
    }
    return false;
  }

  private String ls(String cmd) throws IOException {
    cmd = CommandUtils.parseInlineCommands(cmd);
    StringBuilder out = new StringBuilder();
    String path = Util.makePathString(cmd);
    try (Stream<Path> stream = Files.list(JTerm.getCurrPath().resolve(path))) {
      stream.forEach(p -> out.append(p.getFileName().toString() + "\n"));
    } catch (NoSuchFileException e) {
      Printer.err.println("Error: Path \"" + JTerm.getCurrPath().resolve(path) + "\" couldn't be found!\n");
    } catch (NotDirectoryException e) {
      Printer.err.println(JTerm.getCurrPath().resolve(path) + "\n");
    }
    return Printer.out.println(out);
  }

  private boolean mkdir(String cmd) throws IOException {
    cmd = CommandUtils.parseInlineCommands(cmd);
    Path path = JTerm.getCurrPath().resolve(Util.makePathString(cmd));
    try {
      Files.createDirectory(path);
      return true;
    } catch (FileAlreadyExistsException e) {
      Printer.err.println("Error: Directory \"" + path + "\" already exists!");
    }
    return false;
  }
}
