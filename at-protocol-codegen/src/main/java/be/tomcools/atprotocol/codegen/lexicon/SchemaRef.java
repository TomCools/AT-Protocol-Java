package be.tomcools.atprotocol.codegen.lexicon;

import com.fasterxml.jackson.annotation.JsonValue;

public final class SchemaRef implements Schema {
	@JsonValue
	private String reference;

	public SchemaRef(String reference) {
		this.reference = reference;
	}
}
