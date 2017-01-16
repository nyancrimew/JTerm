package ch.deletescape.jterm.commandcontexts;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import ch.deletescape.jterm.io.Printer;

public class EnvTest {

  @Test
  public void setAnAlias() {
    Env env = new Env();
    assertThat(env.alias("test=echo"), is("Setting alias \"test\" for command \"echo\""));
  }

  @Test
  public void propertiesTest() {
    Env env = new Env();
    env.setProp("test=works");
    assertThat(env.getProp("test"), is("works"));
  }

  @Test
  public void muteTest() {
    Env env = new Env();
    assertThat(Printer.out.isMuted(), is(false));
    assertThat(env.mute(), is(true));
    assertThat(Printer.out.isMuted(), is(true));
  }
}
