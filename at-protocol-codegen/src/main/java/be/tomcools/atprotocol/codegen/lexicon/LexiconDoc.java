package be.tomcools.atprotocol.codegen.lexicon;

import java.util.Map;

public record LexiconDoc(int lexicon, NSID id, Integer revision, String description, Map<String, LexType> defs) {
}
