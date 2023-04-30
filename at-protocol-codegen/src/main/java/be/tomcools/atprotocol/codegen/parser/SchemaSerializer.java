package be.tomcools.atprotocol.codegen.parser;

import be.tomcools.atprotocol.codegen.lexicon.Schema;
import be.tomcools.atprotocol.codegen.lexicon.SchemaRef;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class SchemaSerializer extends JsonSerializer<Schema> {

	@Override
	public void serialize(Schema schema, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
			throws IOException {
		if (schema instanceof SchemaRef) {
			jsonGenerator.writeObject(schema);
		} else {
			jsonGenerator.writeObject(schema);
		}
	}
}
