package ch.deletescape.jterm;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Test;

public class CommandUtilsTest {

  @Test
  public void getArgsWithEcho() {
    assertThat(CommandUtils.getArgs("echo Test"), is("Test"));
  }

  @Test
  public void getArgsWithEchoWithLeadingAndTailingSpaces() {
    assertThat(CommandUtils.getArgs("echo   Test "), is("Test"));
  }

  @Test
  public void testContextLoading() throws IOException {
    CommandUtils.initializeEnv();
    assertThat(CommandUtils.CONTEXTS,
        org.hamcrest.CoreMatchers.hasItems("ch.deletescape.jterm.commandcontexts.Misc",
            "ch.deletescape.jterm.commandcontexts.DateTime", "ch.deletescape.jterm.commandcontexts.Random",
            "ch.deletescape.jterm.commandcontexts.Directories", "ch.deletescape.jterm.commandcontexts.Env",
            "ch.deletescape.jterm.commandcontexts.SingleFiles", "ch.deletescape.jterm.commandcontexts.Network",
            "ch.deletescape.jterm.commandcontexts.scripting.Scripting","ch.deletescape.jterm.commandcontexts.JTerm"));
  }

  @Test
  public void testEvaluateCommand() throws IOException {
    CommandUtils.initializeEnv();
    assertThat(CommandUtils.evaluateCommand("echo Test"), is("Test"));
  }

  @Test
  public void testInlineCommandParsing() throws IOException {
    CommandUtils.initializeEnv();
    assertThat(CommandUtils.parseInlineCommands("Test ${echo tst}"), is("Test tst"));
  }
}
