package be.tomcools.atprotocol.codegen.lexicon;

import java.util.List;
import java.util.Map;

public record LexObject(String description, List<String> required,
		Map<String, LexType> properties) implements LexType, Schema {
}
