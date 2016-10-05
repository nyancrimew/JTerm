package ch.deletescape.jterm;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

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

}
