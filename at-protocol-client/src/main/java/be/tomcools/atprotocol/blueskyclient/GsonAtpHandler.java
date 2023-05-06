package be.tomcools.atprotocol.blueskyclient;

import be.tomcools.atprotocol.client.AtpJsonHandler;
import com.google.gson.Gson;

public class GsonAtpHandler implements AtpJsonHandler {
	private final Gson g = new Gson();

	@Override
	public <T> T fromJson(String input, Class<T> targetClass) {
		return g.fromJson(input, targetClass);
	}
	@Override
	public <T> String toJson(T input) {
		return g.toJson(input);
	}
}
