package be.tomcools.atprotocol.codegen.generator.query;

import be.tomcools.atprotocol.codegen.lexicon.LexXrpcQuery;
import be.tomcools.atprotocol.codegen.lexicon.LexiconDoc;

public record QueryInDocument(LexiconDoc doc, LexXrpcQuery lexQuery) {
};
