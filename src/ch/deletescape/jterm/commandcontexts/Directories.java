package ch.deletescape.jterm.commandcontexts;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;

import ch.deletescape.jterm.CommandUtils;
import ch.deletescape.jterm.JTerm;
import ch.deletescape.jterm.Util;
import ch.deletescape.jterm.io.Printer;

public class Directories implements CommandContext {

  private String pwd() {
    Printer.out.println(JTerm.getCurrPath());
    return JTerm.getCurrPath().toString();
  }

  private boolean cd(String cmd) throws IOException {
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
      Printer.out.println("Error: Path \"" + JTerm.getCurrPath().resolve(path) + "\" couldn't be found!");
    }
    return false;
  }

  private String ls(String cmd) throws IOException {
    StringBuilder out = new StringBuilder();
    String path = Util.makePathString(cmd);
    try {
      Files.list(JTerm.getCurrPath().resolve(path)).forEach(p -> out.append(p.getFileName().toString() + "\n"));
    } catch (NoSuchFileException e) {
      out.append("Error: Path \"" + JTerm.getCurrPath().resolve(path) + "\" couldn't be found!\n");
    } catch (NotDirectoryException e) {
      out.append(JTerm.getCurrPath().resolve(path) + "\n");
    }
    Printer.out.println(out.toString());
    return out.toString();
  }

  private boolean mkdir(String cmd) throws IOException {
    Path path = JTerm.getCurrPath().resolve(Util.makePathString(cmd));
    try {
      Files.createDirectory(path);
    } catch (FileAlreadyExistsException e) {
      Printer.out.println("Error: Directory \"" + path + "\" already exists!");
      return false;
    }
    return true;
  }

  @Override
  public void init() {
    CommandUtils.addListener("cd", (o) -> cd(o));
    CommandUtils.addListener("pwd", (o) -> pwd());
    CommandUtils.addListener("ls", (o) -> ls(o));
    CommandUtils.addListener("dir", (o) -> ls(o));
    CommandUtils.addListener("mkdir", (o) -> mkdir(o));
  }
}
