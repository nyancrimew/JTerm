package ch.deletescape.jterm.commandcontexts;

import java.net.SocketException;

import ch.deletescape.jterm.CommandUtils;
import ch.deletescape.jterm.legacy.IfConfig;

public class Network implements CommandContext {

  private String ifconfig(String cmd) throws SocketException {
    String[] args = "".equals(cmd) ? new String[0] : cmd.split(" ");
    IfConfig.main(args);
    return "Not yet Supported";
  }

  @Override
  public void init() {
    CommandUtils.addListener("ifconfig", (o) -> ifconfig(o));
    CommandUtils.addListener("ipconfig", (o) -> ifconfig(o));
  }
}
