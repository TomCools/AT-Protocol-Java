package be.tomcools.atprotocol.blueskyclient;

public class BlueskyConfiguration {
	private String screenName;
	private String serverName;
	private String password;
	private final String serverUrl;

	public BlueskyConfiguration(String screenName, String password, String serverName, String serverUrl) {
		this.screenName = screenName;
		this.serverName = serverName;
		this.password = password;
		this.serverUrl = serverUrl;
	}

	public String getScreenName() {
		return screenName;
	}

	public String getServerName() {
		return serverName;
	}

	public String getServerUrl() {
		return serverUrl;
	}

	public String getPassword() {
		return password;
	}

	public String getHandle() {
		return screenName.toLowerCase() + "." + serverName;
	}
}
