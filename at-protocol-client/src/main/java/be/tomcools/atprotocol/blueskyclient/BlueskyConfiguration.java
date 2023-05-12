package be.tomcools.atprotocol.blueskyclient;

public class BlueskyConfiguration {
	private final String handle;
	private final String password;
	private final String serverUrl;

	public BlueskyConfiguration(String handle, String password, String serverUrl) {
		if(handle.startsWith("@")) {
			throw new IllegalArgumentException("Use the handle without the @ symbol");
		}
		this.handle = handle;
		this.password = password;
		this.serverUrl = serverUrl;
	}

	public String getHandle() {
		return handle;
	}

	public String getPassword() {
		return password;
	}

	public String getServerUrl() {
		return serverUrl;
	}
}
