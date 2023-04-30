package be.tomcools.atprotocol.codegen.lexicon.types;

import java.util.List;
import java.util.Map;

public record LexQueryParameters(String type, List<String> required, Map<String, Object> properties) {
}
