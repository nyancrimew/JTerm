package ch.deletescape.jterm.commandcontexts;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import javax.script.ScriptException;

import org.junit.Test;

public class MiscTest {

  @Test
  public void simpleCalcTest() throws ScriptException {
    Misc misc = new Misc();
    assertThat(misc.calc("1+1"), is(2d));
  }

  @Test
  public void echoTest() {
    Misc misc = new Misc();
    assertThat(misc.echo("Test"), is("Test"));
  }

  @Test
  public void echoTestWithQuotes() {
    Misc misc = new Misc();
    assertThat(misc.echo("\"Test\""), is("Test"));
  }

}
