package ch.deletescape.jterm.commandcontexts;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import ch.deletescape.jterm.CommandUtils;
import ch.deletescape.jterm.config.Resources;
import ch.deletescape.jterm.io.Printer;

public class Network extends CommandContext {

  @Override
  protected void init() {
    CommandUtils.addListener("ifconfig", this::ifconfig);
  }

  private String ifconfig(String cmd) throws SocketException {
    String command = CommandUtils.parseInlineCommands(cmd);
    boolean showNonConnected = command.contains("-a");
    Printer.out.println(Resources.getString("Network.Title"));
    Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
    while (interfaces.hasMoreElements()) {
      NetworkInterface currInterface = interfaces.nextElement();
      Enumeration<InetAddress> inetAdresses = currInterface.getInetAddresses();
      boolean noConn = !inetAdresses.hasMoreElements();
      if (noConn && !showNonConnected) {
        continue;
      }
      Printer.out.println(Resources.getString("Network.InterfaceNameFormat"), currInterface.getDisplayName(), currInterface.getName());
      Printer.out.println(Resources.getString("Network.PhysicalAddress"), getMacAddress(currInterface));
      Printer.out.println(Resources.getString("Network.Index"), currInterface.getIndex());
      if (noConn) {
        Printer.out.println(Resources.getString("Network.NotConnected"));
      } else {
        Printer.out.println(Resources.getString("Network.MTU"), currInterface.getMTU());
        Printer.out.println(Resources.getString("Network.IPAddresses"));
        while (inetAdresses.hasMoreElements()) {
          Printer.out.println("\t\t%s", inetAdresses.nextElement());
        }
      }
      Printer.out.println();
    }
    return Resources.getString("NonSupported");
  }
  private static String getMacAddress(NetworkInterface ni) throws SocketException {
    if (ni == null) {
      return null;
    }
    byte[] mac = ni.getHardwareAddress();
    if (mac == null) {
      return Resources.getString("Network.MACNotFound");
    }
    StringBuilder sb = new StringBuilder(18);
    for (byte b : mac) {
      if (sb.length() > 0) {
        sb.append(':');
      }
      sb.append(String.format("%02x", b));
    }
    return sb.toString();
  }
}
