package ch.deletescape.jterm.updater;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import ch.deletescape.jterm.config.Resources;
import ch.deletescape.jterm.config.UserProperties;

public class UpdateChecker {
  private static final Logger LOGGER = LogManager.getLogger();

  public Update getLatest() throws IOException {
    String source;
    source = getSource();
    JSONObject json = new JSONObject(source);
    String version = json.getString("tag_name");
    String name = json.getString("name");
    boolean isNewer = remoteVersionIsNewer(Resources.getString("JTerm.Version"), version);
    return new Update(version, name, isNewer);
  }

  private String getSource() throws IOException {
    URLConnection conn = new URL(Resources.getString("JTerm.GitHub.LatestRelease")).openConnection();
    try {
      conn.setConnectTimeout(Integer.parseInt(UserProperties.getProperty("jterm.updatecheck.timeout")));
    } catch (NumberFormatException e) {
      LOGGER.error(e.toString(), e);
    }
    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    StringBuilder sb = new StringBuilder();
    int cp;
    while ((cp = br.read()) != -1) {
      sb.append((char) cp);
    }
    return sb.toString();
  }

  static boolean remoteVersionIsNewer(String version, String remoteVersion) {
    String[] thisParts = version.substring(1).split("\\.");
    String[] thatParts = remoteVersion.substring(1).split("\\.");
    for (int i = 0; i < 3; i++) {
      int thisPart = Integer.parseInt(thisParts[i]);
      int thatPart = Integer.parseInt(thatParts[i]);
      if (thisPart < thatPart) {
        return true;
      }
      if (thisPart > thatPart) {
        return false;
      }
    }
    return false;
  }

  public class Update {
    private String version;
    private String name;
    private boolean isNewer;

    Update(String version, String name, boolean isNewer) {
      this.version = version;
      this.name = name;
      this.isNewer = isNewer;
    }

    public String getVersion() {
      return version;
    }

    public String getName() {
      return name;
    }

    public boolean isNewer() {
      return isNewer;
    }
  }
}
