package ch.deletescape.jterm.commandcontexts;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import ch.deletescape.jterm.CommandUtils;
import ch.deletescape.jterm.JTerm;
import ch.deletescape.jterm.io.Printer;

public class Reflect extends CommandContext {
  private static final BetterURLClassLoader CL = new BetterURLClassLoader(new URL[0]);

  @Override
  protected void init() {
    CommandUtils.addListener("loadjar", this::load);
    CommandUtils.addListener("class", this::clazz);
  }

  private String load(String args) throws MalformedURLException {
    URL url = JTerm.getCurrPath().resolve(args).toUri().toURL();
    CL._addURL(url);
    return "";
  }

  private String clazz(String arg) throws Exception {
    String[] args = arg.split(" ");
    String classname = args[0];
    Printer.out.println("Loading class %s...", classname);
    Class<?> clazz = CL.loadClass(classname);
    Printer.out.println("   Name: %s", clazz.getSimpleName());
    if (args.length > 1) {
      Printer.out.println(" Creating new instance of %s...", clazz.getSimpleName());
      Constructor<?> constr = clazz.getDeclaredConstructor();
      if (!constr.isAccessible()) {
        Printer.out.println("  Default constructor not accessible, forcing access");
        constr.setAccessible(true);
      }
      Object obj = constr.newInstance();
      Printer.out.println(" Getting method %s...", args[1]);
      Method m = clazz.getDeclaredMethod(args[1]);
      if (!m.isAccessible()) {
        Printer.out.println("    Method not accessible, forcing access...");
        m.setAccessible(true);
      }
      Printer.out.println("    Method accessible...");
      Printer.out.println("  Invoking method \"%s\" on previously created object of %s...", m.toString(),
          clazz.toString());
      Object result = m.invoke(obj);
      Printer.out.println("    Success! --- Result: %s", result);
    }
    return "";
  }

  private static class BetterURLClassLoader extends URLClassLoader {

    public BetterURLClassLoader(URL[] urls) {
      super(urls);
    }

    public void _addURL(URL url) {
      addURL(url);
    }
  }
}
