package ch.deletescape.jterm.commandcontexts;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.FileWriter;
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

  @Test
  public void catTest() throws Exception {
    SingleFiles sf = new SingleFiles();
    File file = temp.newFile();
    try (FileWriter fileWriter = new FileWriter(file)) {
      fileWriter.write("test");
    }
    assertThat(sf.cat(file.getAbsolutePath()), is("test"));
  }
  @Test
  public void catOnDirectory() throws Exception {
    SingleFiles sf = new SingleFiles();
    File folder = temp.newFolder();
    try (FileWriter fileWriter = new FileWriter(new File(folder, "test"))) {
      fileWriter.write("test\n");
    }
    try (FileWriter fileWriter = new FileWriter(new File(folder, "test2"))) {
      fileWriter.write("it works");
    }
    assertThat(sf.cat(folder.getAbsolutePath()), is("test\nit works"));
  }
}
