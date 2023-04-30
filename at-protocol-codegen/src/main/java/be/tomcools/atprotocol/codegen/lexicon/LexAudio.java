package be.tomcools.atprotocol.codegen.lexicon;

import java.util.List;

public record LexAudio(String description, List<String> accept, Integer maxSize, Integer maxLength) implements LexType {
}
