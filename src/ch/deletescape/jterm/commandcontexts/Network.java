package ch.deletescape.jterm.commandcontexts;

import java.net.SocketException;

import ch.deletescape.jterm.CommandUtils;
import ch.deletescape.jterm.legacy.IfConfig;

public class Network implements CommandContext {

  @Override
  public void init() {
    CommandUtils.addListener("ifconfig", this::ifconfig);
    CommandUtils.addListener("ipconfig", this::ifconfig);
  }

  private String ifconfig(String cmd) throws SocketException {
    cmd = CommandUtils.parseInlineCommands(cmd);
    String[] args = "".equals(cmd) ? new String[0] : cmd.split(" ");
    IfConfig.main(args);
    return "Not yet Supported";
  }
}
