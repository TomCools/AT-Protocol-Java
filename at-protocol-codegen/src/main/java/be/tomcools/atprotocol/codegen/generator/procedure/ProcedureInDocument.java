package be.tomcools.atprotocol.codegen.generator.procedure;

import be.tomcools.atprotocol.codegen.lexicon.LexXrpcProcedure;
import be.tomcools.atprotocol.codegen.lexicon.LexiconDoc;

public record ProcedureInDocument(LexiconDoc doc, LexXrpcProcedure lexProcedure) {
};
