package ch.deletescape.jterm.commandcontexts;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import javax.script.ScriptException;

import org.junit.Test;

public class ScriptingTest {

  @Test
  public void evalTrue_returnsTrue() throws ScriptException {
    Scripting scripting = new Scripting();
    assertThat(scripting.eval("true"), is(true));
  }

  @Test
  public void eval1isNot1_returnsFalse() throws ScriptException {
    Scripting scripting = new Scripting();
    assertThat(scripting.eval("1!=1"), is(false));
  }

  @Test
  public void varAndGetVarTest() throws ScriptException {
    Scripting scripting = new Scripting();
    assertThat(scripting.var("i=1"), is(1));
    assertThat(scripting.getVar("i"), is(1));
  }

  @Test
  public void nonIntegerOrBooleanVarIsString() throws ScriptException {
    Scripting scripting = new Scripting();
    assertThat(scripting.var("i=abcd"), is("abcd"));
  }
}
