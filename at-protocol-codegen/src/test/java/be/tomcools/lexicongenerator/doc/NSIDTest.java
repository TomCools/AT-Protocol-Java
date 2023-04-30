package be.tomcools.lexicongenerator.doc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import be.tomcools.atprotocol.codegen.lexicon.NSID;
import org.junit.jupiter.api.Test;

class NSIDTest {

	@Test
	public void givenValidNSIDString_canRetrieveAuthority() {
		NSID nsid = new NSID("com.example.status");

		assertEquals(nsid.getAuthority(), "example.com");
	}

	@Test
	public void givenValidNSIDString_canRetrieveName() {
		NSID nsid = new NSID("com.example.status");

		assertEquals(nsid.getName(), "status");
	}

	@Test
	public void givenValidNSIDString_canRetrieveDomain() {
		NSID nsid = new NSID("com.example.status");

		assertEquals(nsid.getDomain(), "com.example");
	}
}
