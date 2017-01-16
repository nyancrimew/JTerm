package ch.deletescape.jterm.config;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class ResourcesTest {

  @Test
  public void getString() {
    assertThat(Resources.getString("test"), is("seems to work"));
  }

  @Test
  public void getHelp() {
    assertThat(Resources.getHelp("test", true), is("short help"));
    assertThat(Resources.getHelp("test", false), is("long help"));
  }

}
