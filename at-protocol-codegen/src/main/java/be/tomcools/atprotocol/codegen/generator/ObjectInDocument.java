package be.tomcools.atprotocol.codegen.generator;

import be.tomcools.atprotocol.codegen.lexicon.LexObject;
import be.tomcools.atprotocol.codegen.lexicon.LexiconDoc;

public record ObjectInDocument(LexiconDoc doc, String name, LexObject obj) {
};
