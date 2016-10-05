package ch.deletescape.jterm.legacy;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import ch.deletescape.jterm.io.Printer;

public class IfConfig {
  public static void main(String[] args) throws SocketException {
    List<String> argsList = Arrays.asList(args);
    if (!argsList.contains("--help") && !argsList.contains("-h") && (argsList.isEmpty() || argsList.contains("-a"))) {
      boolean showNonConnected = argsList.contains("-a");
      Printer.out.println("\nNetwork Configuration\n");
      Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
      while (interfaces.hasMoreElements()) {
        NetworkInterface currInterface = interfaces.nextElement();
        Enumeration<InetAddress> inetAdresses = currInterface.getInetAddresses();
        boolean noConn = !inetAdresses.hasMoreElements();
        if (noConn && !showNonConnected) {
          continue;
        }
        Printer.out.println(currInterface.getDisplayName() + " (" + currInterface.getName() + "):");
        Printer.out.println("\tPhysical Address: " + getMacAddress(currInterface));
        Printer.out.println("\tIndex: " + currInterface.getIndex());
        if (noConn) {
          Printer.out.println("\tNot connected");
        } else {
          Printer.out.println("\tMTU: " + currInterface.getMTU());
          Printer.out.println("\tHostnames / IP-Adresses:");
          while (inetAdresses.hasMoreElements()) {
            Printer.out.println("\t\t" + inetAdresses.nextElement().toString());
          }
        }
        Printer.out.println();
      }
    } else {
      Printer.out.println("\nUsage:");
      Printer.out.println("\tifconfig -h / --help:\tShow this usage info");
      Printer.out.println("\tifconfig -a:\t\tInclude not connected interfaces in the result");
      Printer.out.println("\nThis is a Java ifconfig / ipconfig clone by Deletescape Media\n");
    }
  }

  public static String getMacAddress(NetworkInterface ni) throws SocketException {
    if (ni == null)
      return null;

    byte[] mac = ni.getHardwareAddress();
    if (mac == null)
      return "not found";

    StringBuilder sb = new StringBuilder(18);
    for (byte b : mac) {
      if (sb.length() > 0)
        sb.append(':');
      sb.append(String.format("%02x", b));
    }
    return sb.toString();
  }
}
