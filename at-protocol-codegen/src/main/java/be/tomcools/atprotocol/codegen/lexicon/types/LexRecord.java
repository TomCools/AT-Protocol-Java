package be.tomcools.atprotocol.codegen.lexicon.types;

import be.tomcools.atprotocol.codegen.lexicon.LexObject;
import be.tomcools.atprotocol.codegen.lexicon.LexType;

public record LexRecord(String description, String key, LexObject record) implements LexType {
}
