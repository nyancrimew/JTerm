package ch.deletescape.jterm.commandcontexts;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

import ch.deletescape.jterm.CommandUtils;
import ch.deletescape.jterm.JTerm;
import ch.deletescape.jterm.Util;
import ch.deletescape.jterm.io.Printer;

public class SingleFiles implements CommandContext {
  private boolean rm(String cmd) throws IOException {
    try {
      Path path = JTerm.getCurrPath().resolve(Util.makePathString(cmd)).toRealPath();
      Files.delete(path);
    } catch (NoSuchFileException e) {
      Printer.out
          .println("Error: Path \"" + JTerm.getCurrPath().resolve(Util.makePathString(cmd)) + "\" couldn't be found!");
      return false;
    }
    return true;
  }

  private String cat(String cmd) throws IOException {
    StringBuilder sb = new StringBuilder();
    try {
      Path path = JTerm.getCurrPath().resolve(Util.makePathString(cmd)).toRealPath();
      if (Files.isDirectory(path)) {
        Files.list(path).filter(p -> !Files.isDirectory(p)).forEach(p -> sb.append(printFile(p)));
      } else {
        sb.append(printFile(path));
      }
    } catch (NoSuchFileException e) {
      Printer.err
          .println("Error: Path \"" + JTerm.getCurrPath().resolve(Util.makePathString(cmd)) + "\" couldn't be found!");
    }
    return sb.toString();
  }

  private String printFile(Path path) {
    StringBuilder sb = new StringBuilder();
    try {
      try (InputStream in = Files.newInputStream(path)) {
        sb.append(Util.copyStream(in, Printer.out.getPrintStream()));
      }
    } catch (IOException e) {
      Printer.err.println(e);
    }
    return sb.toString();
  }

  private Object write(String args) throws IOException {
    String content = args.split(">")[0].trim();
    String destination = args.split(">")[1].trim();
    Path dest = JTerm.getCurrPath().resolve(Util.makePathString(destination));
    try (BufferedWriter bw = Files.newBufferedWriter(dest)) {
      bw.write(content);
    }
    return null;
  }

  @Override
  public void init() {
    CommandUtils.addListener("rm", (o) -> rm(o));
    CommandUtils.addListener("cat", (o) -> cat(o));
    CommandUtils.addListener("write", this::write);
  }
}
