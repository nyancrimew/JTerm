package ch.deletescape.jterm.commandcontexts;

import java.net.SocketException;

import ch.deletescape.jterm.CommandUtils;
import ch.deletescape.jterm.config.Resources;
import ch.deletescape.jterm.legacy.IfConfig;

public class Network extends CommandContext {

  @Override
  public void init() {
    CommandUtils.addListener("ifconfig", this::ifconfig);
    CommandUtils.addListener("ipconfig", this::ifconfig);
  }

  private String ifconfig(String cmd) throws SocketException {
    String command = CommandUtils.parseInlineCommands(cmd);
    String[] args = command.isEmpty() ? new String[0] : command.split(" ");
    IfConfig.main(args);
    return Resources.getString("NonSupported");
  }
}
