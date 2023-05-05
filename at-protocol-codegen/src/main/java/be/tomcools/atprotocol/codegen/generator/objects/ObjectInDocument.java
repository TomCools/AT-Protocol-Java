package be.tomcools.atprotocol.codegen.generator.objects;

import be.tomcools.atprotocol.codegen.lexicon.LexObject;
import be.tomcools.atprotocol.codegen.lexicon.LexiconDoc;

public record ObjectInDocument(LexiconDoc doc, String objectKey, LexObject obj) {
};
