package be.tomcools.atprotocol.codegen.lexicon.types;

import be.tomcools.atprotocol.codegen.lexicon.LexType;
import java.util.List;

public record LexBlob(String description, List<String> accept, Integer maxSize) implements LexType {
}
