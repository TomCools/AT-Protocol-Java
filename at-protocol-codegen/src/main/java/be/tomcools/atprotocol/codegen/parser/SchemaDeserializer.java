package be.tomcools.atprotocol.codegen.parser;

import be.tomcools.atprotocol.codegen.lexicon.LexObject;
import be.tomcools.atprotocol.codegen.lexicon.Schema;
import be.tomcools.atprotocol.codegen.lexicon.SchemaRef;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;

public class SchemaDeserializer extends JsonDeserializer<Schema> {

	@Override
	public Schema deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {

		ObjectCodec codec = jp.getCodec();

		JsonNode node = codec.readTree(jp);

		return switch (node.getNodeType()) {
			case STRING -> codec.treeToValue(node, SchemaRef.class);
			case OBJECT -> codec.treeToValue(node, LexObject.class);
			default -> throw new JsonMappingException(jp, "Invalid value for the \"type\" property");
		};
	}
}
