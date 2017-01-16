package ch.deletescape.jterm.commandcontexts;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.deletescape.jterm.CommandUtils;
import ch.deletescape.jterm.JTerm;
import ch.deletescape.jterm.Util;
import ch.deletescape.jterm.config.Resources;
import ch.deletescape.jterm.io.Printer;

public class Directories extends CommandContext {
  private static final Logger LOGGER = LogManager.getLogger();

  @Override
  protected void init() {
    CommandUtils.addListener("cd", this::cd);
    CommandUtils.addListener("pwd", o -> pwd());
    CommandUtils.addListener("ls", this::ls);
    CommandUtils.addListener("mkdir", this::mkdir);
  }

  private String pwd() {
    return Printer.out.println(JTerm.getCurrPath());
  }

  String cd(String cmd) throws IOException {
    String command = CommandUtils.parseInlineCommands(cmd);
    Path path = JTerm.getCurrPath().resolve(Util.makePathString(command)).toRealPath();
    if (Files.isDirectory(path)) {
      JTerm.setCurrPath(path);
      return pwd();
    }
    return Printer.err.println(Resources.getString("Directories.PathIsNotDirectory"), path);
  }

  String ls(String cmd) throws IOException {
    String command = CommandUtils.parseInlineCommands(cmd);
    Path path = JTerm.getCurrPath().resolve(Util.makePathString(command));
    if (!Files.exists(path)) {
      return Printer.err.println(Resources.getString("PathNotFound"), path);
    }
    if (!Files.isDirectory(path)) {
      return Printer.out.println(path);
    }
    try (Stream<Path> stream = Files.list(path)) {
      StringBuilder out = new StringBuilder();
      stream.forEach(p -> out.append(p.getFileName().toString() + "\n"));
      return Printer.out.println(out);
    }
  }

  String mkdir(String cmd) throws IOException {
    String command = CommandUtils.parseInlineCommands(cmd);
    Path path = JTerm.getCurrPath().resolve(Util.makePathString(command));
    try {
      Files.createDirectory(path);
      return path.toString();
    } catch (FileAlreadyExistsException e) {
      LOGGER.error(e.toString(), e);
      return Printer.err.println(Resources.getString("Directories.DirectoryAlreadyExists"), path);
    }
  }
}
