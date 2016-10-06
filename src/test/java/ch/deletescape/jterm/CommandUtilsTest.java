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
  public void testEvaluateCommand() throws IOException {
    CommandUtils.initializeEnv();
    assertThat(CommandUtils.evaluateCommand("echo Test"), is("Test"));
  }
  @Test
  public void testInlineCommandParsing() throws IOException  {
    CommandUtils.initializeEnv();
    assertThat(CommandUtils.parseInlineCommands("Test ${echo tst}"),is("Test tst"));
  }
}
