package ch.deletescape.jterm.commandcontexts;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class SingleFilesTest {
  @Rule
  public TemporaryFolder temp = new TemporaryFolder();

  @Test
  public void rmTest() throws IOException {
    File file = temp.newFile();
    SingleFiles sf = new SingleFiles();
    sf.rm(file.getAbsolutePath());
    assertThat(file.exists(), is(false));
  }

  @Test
  public void rmNonExistentFile() throws IOException {
    Locale.setDefault(Locale.ROOT);
    File file = new File(temp.newFolder(), "test");
    SingleFiles sf = new SingleFiles();
    String path = file.getAbsolutePath();
    assertThat(sf.rm(path), is("Error: Path \"" + path + "\" couldn't be found!"));
  }

}
