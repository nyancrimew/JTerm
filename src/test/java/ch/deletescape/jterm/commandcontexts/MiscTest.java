package ch.deletescape.jterm.commandcontexts;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import javax.script.ScriptException;

import org.junit.Test;

public class MiscTest {

  @Test
  public void simpleCalcTest() throws ScriptException {
    Misc misc = new Misc();
    assertThat(misc.calc("1+1"), is(2));
  }

  @Test
  public void calcWithVariables() throws ScriptException {
    Misc misc = new Misc();
    misc.calc("var i=1");
    assertThat(misc.calc("++i"), is(2d));
  }

}
