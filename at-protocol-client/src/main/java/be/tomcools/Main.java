package be.tomcools;

import be.tomcools.atprotocol.client.AtpApi;
import com.atproto.server.createsession.CreateSessionInput;
import com.atproto.server.createsession.CreateSessionOutput;

// Dogfooding ;)
public class Main {
	public static void main(String[] args) {
		AtpApi s = new AtpApi();
		CreateSessionOutput createSessionOutput = s.createSession(new CreateSessionInput("Tom", "Cools"));



	}
}