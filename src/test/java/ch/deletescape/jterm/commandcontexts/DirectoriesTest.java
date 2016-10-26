package ch.deletescape.jterm.commandcontexts;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class DirectoriesTest {
  @Rule
  public TemporaryFolder tempFolder = new TemporaryFolder();

  @Test
  public void cdTest() throws IOException {
    Directories directories = new Directories();
    String path = tempFolder.newFolder().getAbsolutePath();

    assertThat(directories.cd(path), is(path));
  }

  @Test
  public void lsTest() throws Exception {
    Directories directories = new Directories();
    File folder = tempFolder.newFolder();
    new File(folder, "test").createNewFile();
    new File(folder, "test2").createNewFile();
    String path = folder.getAbsolutePath();

    assertThat(directories.ls(path), is("test\ntest2\n"));
  }

  @Test
  public void mkdirTest() throws Exception {
    Directories directories = new Directories();
    File folder = tempFolder.newFolder();
    File newFolder = new File(folder, "test");

    directories.mkdir(newFolder.getAbsolutePath());

    assertThat(newFolder.exists(), is(true));
  }
}
