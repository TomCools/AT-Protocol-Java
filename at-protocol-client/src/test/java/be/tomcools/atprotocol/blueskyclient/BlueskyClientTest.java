package be.tomcools.atprotocol.blueskyclient;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

import be.tomcools.atprotocol.client.AtpApiException;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@WireMockTest(httpsEnabled = true)
class BlueskyClientTest {
	@Test
	void givenValidStringAndConfiguration_shouldBeAbleToPostToBluesky(WireMockRuntimeInfo wmRuntimeInfo) {
		String TEST_STRING = "TEST_" + UUID.randomUUID();

		BlueskyClient client = setupClient(wmRuntimeInfo);

		client.post(TEST_STRING);

		wmRuntimeInfo.getWireMock()
				.verifyThat(RequestPatternBuilder.newRequestPattern().withUrl("/xrpc/com.atproto.repo.createRecord")
						.withHeader("Authorization", equalTo("Bearer TEST_ACCESS_JWT"))
						.withRequestBody(containing(TEST_STRING)));
	}

	@Test
	void givenValidStringAndConfiguration_ifHandleIsNotKnown_shouldThrowException(WireMockRuntimeInfo wmRuntimeInfo) {
		String TEST_STRING = "TEST_" + UUID.randomUUID();
		BlueskyClient client = setupClient(wmRuntimeInfo);

		stubFor(get(urlMatching(".*resolveHandle.*")).willReturn(serverError()));

		Assertions.assertThrows(AtpApiException.class, () -> {
			client.post(TEST_STRING);
		});
	}

	private static BlueskyClient setupClient(WireMockRuntimeInfo wmRuntimeInfo) {
		BlueskyConfiguration configuration = new BlueskyConfiguration("TomCools", "fakepass", "localhost",
				wmRuntimeInfo.getHttpBaseUrl());
		return new BlueskyClient(configuration);
	}

}