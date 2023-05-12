package be.tomcools.atprotocol.blueskyclient;


import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

public class BlueskyRealTest {
    @Test
    @Disabled //Disabled because I test this manually, with local environment variables :p
    public void testRealPost() {
        BlueskyClient client = setupClient();

        client.post("TESTing from #java Code ;)");
    }

    @Test
    @Disabled //Disabled because I test this manually, with local environment variables :p
    public void testRealPostThread() {
        BlueskyClient client = setupClient();

        client.postThread(List.of("I must say... bluesky is pretty relaxing so far. Way less noise!","Let's hope more of my beloved #java friends will fiend their way here! Kinda getting overwhelmed with Twitter, Mastodon AND Bluesky."));
    }

    private static BlueskyClient setupClient() {
        BlueskyConfiguration configuration = new BlueskyConfiguration("tcoolsit.bsky.social", "hzwj-wymc-7me6-46rl", "https://bsky.social");
        return new BlueskyClient(configuration);
    }
}
