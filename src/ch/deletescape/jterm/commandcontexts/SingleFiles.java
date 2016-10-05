package ch.deletescape.jterm.commandcontexts;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.stream.Stream;

import ch.deletescape.jterm.CommandUtils;
import ch.deletescape.jterm.JTerm;
import ch.deletescape.jterm.Util;
import ch.deletescape.jterm.io.Printer;

public class SingleFiles implements CommandContext {
  @Override
  public void init() {
    CommandUtils.addListener("rm", this::rm);
    CommandUtils.addListener("cat", this::cat);
    CommandUtils.addListener("write", this::write);
  }

  private boolean rm(String cmd) throws IOException {
    String command = CommandUtils.parseInlineCommands(cmd);
    Path path = JTerm.getCurrPath().resolve(Util.makePathString(command)).toRealPath();
    try {
      Files.delete(path);
      return true;
    } catch (NoSuchFileException e) {
      Printer.out.println("Error: Path \"" + path + "\" couldn't be found!");
    }
    return false;
  }

  private String cat(String cmd) throws IOException {
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

  private String printFile(Path path) {
    StringBuilder sb = new StringBuilder();
    try (InputStream in = Files.newInputStream(path)) {
      sb.append(Util.copyStream(in, Printer.out.getPrintStream()));
    } catch (IOException e) {
      Printer.err.println(e);
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
