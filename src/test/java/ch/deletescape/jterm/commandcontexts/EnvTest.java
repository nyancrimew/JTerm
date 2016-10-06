package ch.deletescape.jterm.commandcontexts;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class EnvTest {

  @Test
  public void setAnAlias() {
    Env env = new Env();
    assertThat(env.alias("test=echo"),is("Setting alias \"test\" for command \"echo\""));
  }

}
