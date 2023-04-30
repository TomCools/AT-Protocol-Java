package be.tomcools.atprotocol.codegen.lexicon.types;

import be.tomcools.atprotocol.codegen.lexicon.Schema;
import be.tomcools.atprotocol.codegen.parser.SchemaDeserializer;
import be.tomcools.atprotocol.codegen.parser.SchemaSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public record LexXrpcBody(String description, String encoding,
		@JsonDeserialize(using = SchemaDeserializer.class) @JsonSerialize(using = SchemaSerializer.class) Schema schema) {
}
