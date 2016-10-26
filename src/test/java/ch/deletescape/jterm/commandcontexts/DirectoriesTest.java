package ch.deletescape.jterm.commandcontexts;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

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
  public void cdIntoFile() throws IOException {
    Locale.setDefault(Locale.ROOT);
    Directories directories = new Directories();
    String path = tempFolder.newFile().getAbsolutePath();

    assertThat(directories.cd(path), is(path + " is not a directory"));
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
  public void lsOnFileTest() throws Exception {
    Directories directories = new Directories();
    File folder = tempFolder.newFolder();
    File file = new File(folder, "test");
    file.createNewFile();
    String path = file.getAbsolutePath();

    assertThat(directories.ls(path), is(path));
  }

  @Test
  public void lsOnNotExistingPath() throws Exception {
    Locale.setDefault(Locale.ROOT);
    Directories directories = new Directories();
    File folder = tempFolder.newFolder();
    String path = new File(folder, "test").getAbsolutePath();

    assertThat(directories.ls(path), is("Error: Path \"" + path + "\" couldn't be found!"));
  }

  @Test
  public void mkdirTest() throws Exception {
    Directories directories = new Directories();
    File folder = tempFolder.newFolder();
    File newFolder = new File(folder, "test");

    directories.mkdir(newFolder.getAbsolutePath());

    assertThat(newFolder.exists(), is(true));
  }

  @Test
  public void mkdirOnAlreadyExistingFolder() throws Exception {
    Locale.setDefault(Locale.ROOT);
    Directories directories = new Directories();
    File folder = tempFolder.newFolder();
    File newFolder = new File(folder, "test");
    newFolder.createNewFile();
    String path = newFolder.getAbsolutePath();
    assertThat(directories.mkdir(path), is("Error: Directory \"" + path + "\" already exists!"));
  }
}
