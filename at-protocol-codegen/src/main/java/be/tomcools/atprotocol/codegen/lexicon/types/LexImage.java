package be.tomcools.atprotocol.codegen.lexicon.types;

import java.util.List;

public record LexImage(String description, List<String> accept, Integer maxSize, Integer maxWidth, Integer maxHeight) {
}
