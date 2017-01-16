package ch.deletescape.jterm.config;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class UserPropertiesTest {

  @Test
  public void setProperty() {
    String key = "test" + Math.random();
    UserProperties.setProperty(key, "works");
    assertThat(UserProperties.getProperty(key), is("works"));
  }

  @Test
  public void getBooleanPropertyIsTrue() {
    String key = "test" + Math.random();
    UserProperties.setProperty(key, "true");
    assertThat(UserProperties.getBoolean(key), is(true));
    key = "test" + Math.random();
    UserProperties.setProperty(key, "1");
    assertThat(UserProperties.getBoolean(key), is(true));
  }

  @Test
  public void getBooleanPropertyIsFalse() {
    String key = "test" + Math.random();
    UserProperties.setProperty(key, "false");
    assertThat(UserProperties.getBoolean(key), is(false));
    key = "test" + Math.random();
    UserProperties.setProperty(key, "0");
    assertThat(UserProperties.getBoolean(key), is(false));
    key = "test" + Math.random();
    UserProperties.setProperty(key, "ab");
    assertThat(UserProperties.getBoolean(key), is(false));
  }
}
