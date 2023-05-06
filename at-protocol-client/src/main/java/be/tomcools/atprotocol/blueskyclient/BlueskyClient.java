package be.tomcools.atprotocol.blueskyclient;

import app.bsky.feed.post.PostRecord;
import be.tomcools.atprotocol.client.AtpApi;
import com.atproto.repo.createrecord.CreateRecordInput;
import com.atproto.server.createsession.CreateSessionInput;
import com.atproto.server.createsession.CreateSessionOutput;
import java.io.IOException;
import java.time.LocalDate;

public class BlueskyClient {
	private final BlueskyConfiguration configuration;
	private final AtpApi api;

	public BlueskyClient(BlueskyConfiguration configuration) {
		this.configuration = configuration;
		this.api = AtpApi.service(configuration.getServerUrl(), new GsonAtpHandler());
	}

	public void post(String text) {
		try {
			String did = api.resolveHandle(configuration.getHandle()).getDid();
			CreateSessionOutput createSessionOutput = api
					.createSession(new CreateSessionInput(did, configuration.getPassword()));
			api.setHeader("Authorization", "Bearer: " + createSessionOutput.getAccessJwt());

			api.createRecord(
					new CreateRecordInput(did, "app.bsky.feed.post", new PostRecord(text, LocalDate.now().toString())));
		} catch (IOException | InterruptedException e) {
			throw new BlueskyException(e);
		}
	}
}
