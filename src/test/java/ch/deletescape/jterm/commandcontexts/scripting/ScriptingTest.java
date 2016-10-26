package ch.deletescape.jterm.commandcontexts.scripting;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import javax.script.ScriptException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ScriptingTest {
  @Rule
  public ExpectedException thrown = ExpectedException.none();

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
  public void varTest() throws Exception {
    Scripting scripting = new Scripting();
    assertThat(scripting.var("i=1"), is(1));
    assertThat(scripting.var("i"), is(1));
  }

  @Test
  public void nonIntegerOrBooleanVarIsString() throws Exception {
    Scripting scripting = new Scripting();
    assertThat(scripting.var("i=abcd"), is("abcd"));
  }

  @Test
  public void validVariablenameIsValid() throws ScriptingException {
    Scripting scripting = new Scripting();
    scripting.validateVariableName("var123");
  }

  public void invalidVariablenameIsInvalid() throws ScriptingException {
    thrown.expect(Exception.class);
    Scripting scripting = new Scripting();
    scripting.validateVariableName("1var123");
  }
}
