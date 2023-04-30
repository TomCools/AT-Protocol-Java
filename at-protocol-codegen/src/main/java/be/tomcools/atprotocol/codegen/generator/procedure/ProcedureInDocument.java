package be.tomcools.atprotocol.codegen.generator.procedure;

import be.tomcools.atprotocol.codegen.lexicon.LexXrpcProcedure;
import be.tomcools.atprotocol.codegen.lexicon.LexiconDoc;
import be.tomcools.atprotocol.codegen.lexicon.NSID;

public record ProcedureInDocument(LexiconDoc doc, String name, LexXrpcProcedure obj) {

    /**
     * This method will return the expected package objectKey. Normally this is the
     * document id. However, if the class objectKey is also the objectKey of the document,
     * just use the domain.
     * @return the expected package objectKey
     */
    public String determineFQClassName() {
        NSID identifier = doc.id();
        return identifier.getDomain();
    }

    /**
     * @return the expected method name;
     */
    public String determineMethodName() {
        // schema should not be taken into account for the objectKey, so remove it.
        String[] split = name.replace(".schema", "").split("\\.");
        return split[split.length - 1];
    }
};
