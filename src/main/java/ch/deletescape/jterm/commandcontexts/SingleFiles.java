package ch.deletescape.jterm.commandcontexts;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.deletescape.jterm.CommandUtils;
import ch.deletescape.jterm.JTerm;
import ch.deletescape.jterm.Util;
import ch.deletescape.jterm.config.Resources;
import ch.deletescape.jterm.io.Printer;

public class SingleFiles extends CommandContext {
  private static final Logger LOGGER = LogManager.getLogger();

  @Override
  protected void init() {
    CommandUtils.addListener("rm", this::rm);
    CommandUtils.addListener("cat", this::cat);
    CommandUtils.addListener("write", this::write);
  }

  String rm(String cmd) throws IOException {
    String command = CommandUtils.parseInlineCommands(cmd);
    try {
      Path path = JTerm.getCurrPath().resolve(Util.makePathString(command)).toRealPath();
      Files.delete(path);
      return path.toString();
    } catch (NoSuchFileException e) {
      LOGGER.error(e.toString(), e);
      return Printer.out.println(Resources.getString("PathNotFound"), cmd);
    }
  }

  String cat(String cmd) throws IOException {
    String command = CommandUtils.parseInlineCommands(cmd);
    StringBuilder sb = new StringBuilder();
    Path path = JTerm.getCurrPath().resolve(Util.makePathString(command)).toRealPath();
    if (Files.isDirectory(path)) {
      try (Stream<Path> stream = Files.list(path)) {
        stream.filter(p -> !Files.isDirectory(p)).forEach(p -> sb.append(printFile(p)));
      }
    } else {
      sb.append(printFile(path));
    }
    return sb.toString();
  }

  String printFile(Path path) {
    StringBuilder sb = new StringBuilder();
    try (InputStream in = Files.newInputStream(path)) {
      sb.append(Util.copyStream(in, Printer.out.getPrintStream()));
    } catch (IOException e) {
      Printer.err.println(e);
      LOGGER.error(e.toString(), e);
    }
    return sb.toString();
  }

  private String write(String args) throws IOException {
    String arguments = CommandUtils.parseInlineCommands(args);
    String content = arguments.split(" > ")[0];
    String destination = arguments.split(" > ")[1];
    Path dest = JTerm.getCurrPath().resolve(Util.makePathString(destination));
    try (BufferedWriter bw = Files.newBufferedWriter(dest)) {
      bw.write(content);
    }
    return content;
  }
}
