package be.tomcools.atprotocol.blueskyclient;

import app.bsky.feed.post.PostRecord;
import app.bsky.feed.post.ReplyRef;
import be.tomcools.atprotocol.client.AtpApi;
import com.atproto.repo.StrongRef;
import com.atproto.repo.createrecord.CreateRecordInput;
import com.atproto.repo.createrecord.CreateRecordOutput;
import com.atproto.server.createsession.CreateSessionInput;
import com.atproto.server.createsession.CreateSessionOutput;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class BlueskyClient {
	private final BlueskyConfiguration configuration;
	private final AtpApi api;

	public BlueskyClient(BlueskyConfiguration configuration) {
		this.configuration = configuration;
		this.api = AtpApi.service(configuration.getServerUrl(), new JacksonAtpHandler());
	}

	public void post(String text) {
		try {
			String did = api.resolveHandle(configuration.getHandle()).getDid();
			CreateSessionOutput createSessionOutput = api
					.createSession(new CreateSessionInput(configuration.getHandle(), configuration.getPassword()));
			api.setHeader("Authorization", "Bearer " + createSessionOutput.getAccessJwt());

			PostRecord postRecord = PostRecord.from(text, LocalDateTime.now()).build();
			post(did, postRecord);
		} catch (IOException | InterruptedException e) {
			throw new BlueskyException(e);
		}
	}

	public void postThread(List<String> posts) {
		try {
			String did = api.resolveHandle(configuration.getHandle()).getDid();
			CreateSessionOutput createSessionOutput = api
					.createSession(new CreateSessionInput(configuration.getHandle(), configuration.getPassword()));
			api.setHeader("Authorization", "Bearer " + createSessionOutput.getAccessJwt());

			CreateRecordOutput firstPost = null;
			CreateRecordOutput previous = null;
			for(int i = 0; i < posts.size(); i++) {
				String post = posts.get(i);
				if(i == 0) {
					PostRecord postRecord = PostRecord.from(post, LocalDateTime.now()).build();
					CreateRecordOutput output = post(did, postRecord);
					firstPost = output;
					previous = output;
				} else {
					PostRecord postRecord = PostRecord.from(post, LocalDateTime.now())
							.reply(ReplyRef.builder()
									.root(StrongRef.from(firstPost.getUri(), firstPost.getCid()).build())
									.parent(StrongRef.from(previous.getUri(), previous.getCid()).build())
									.build()
							).build();
					previous = this.post(did, postRecord);
				}

			}
		} catch (IOException | InterruptedException e) {
			throw new BlueskyException(e);
		}
	}

	private CreateRecordOutput post(String did, PostRecord postRecord) throws IOException, InterruptedException {
		return api.createRecord(CreateRecordInput.from(did, "app.bsky.feed.post", postRecord).build());
	}
}
