package ch.deletescape.jterm.legacy;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

public class IfConfig {
  public static void main(String[] args) throws SocketException {
    List<String> argsList = Arrays.asList(args);
    if (!argsList.contains("--help") && !argsList.contains("-h") && (argsList.isEmpty() || argsList.contains("-a"))) {
      boolean showNonConnected = argsList.contains("-a");
      System.out.println("\nNetwork Configuration\n");
      Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
      while (interfaces.hasMoreElements()) {
        NetworkInterface currInterface = interfaces.nextElement();
        Enumeration<InetAddress> inetAdresses = currInterface.getInetAddresses();
        boolean noConn = !inetAdresses.hasMoreElements();
        if (noConn && !showNonConnected) {
          continue;
        }
        System.out.println(currInterface.getDisplayName() + " (" + currInterface.getName() + "):");
        System.out.println("\tPhysical Address: " + getMacAddress(currInterface));
        System.out.println("\tIndex: " + currInterface.getIndex());
        if (noConn) {
          System.out.println("\tNot connected");
        } else {
          System.out.println("\tMTU: " + currInterface.getMTU());
          System.out.println("\tHostnames / IP-Adresses:");
          while (inetAdresses.hasMoreElements()) {
            System.out.println("\t\t" + inetAdresses.nextElement().toString());
          }
        }
        System.out.println("");
      }
    } else {
      System.out.println("\nUsage:");
      System.out.println("\tifconfig -h / --help:\tShow this usage info");
      System.out.println("\tifconfig -a:\t\tInclude not connected interfaces in the result");
      System.out.println("\nThis is a Java ifconfig / ipconfig clone by Till Kottmann\n");
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
