package ch.deletescape.jterm;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import ch.deletescape.jterm.io.Printer;

public class JTerm {
  private static String usr = System.getProperty("user.name");
  private static String home = System.getProperty("user.home");
  private static Path currPath = Paths.get(System.getProperty("user.dir"));
  private static boolean isRunning = true;
  private static Scanner scan = new Scanner(System.in);
  private static final ScriptEngineManager SCRIPT_MANAGER = new ScriptEngineManager();
  private static final ScriptEngine JSENGINE = SCRIPT_MANAGER.getEngineByName("js");

  public static void main(String[] args) throws IOException {
    if (home == null) {
      home = System.getProperty("user.dir");
    }
    CommandUtils.initializeEnv();
    Printer.out.println("JTerm, a simple terminal written in Java by Till Kottmann");
    if (args.length > 0) {
      CommandUtils.evaluateCommand(String.join(" ", args));
    }
    while (isRunning) {
      CommandUtils.printInputPromt();
      CommandUtils.evaluateCommand(scan.nextLine());
    }
    scan.close();
  }

  public static boolean isRunning() {
    return isRunning;
  }

  public static Scanner getScanner() {
    return scan;
  }

  public static String getUsr() {
    return usr;
  }

  public static String getHome() {
    return home;
  }

  public static Path getCurrPath() {
    return currPath;
  }

  public static void setCurrPath(Path currPath) {
    JTerm.currPath = currPath;
  }

  public static ScriptEngine getJsEngine() {
    return JSENGINE;
  }

  public static void exit() {
    isRunning = false;
  }
}
