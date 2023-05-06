package be.tomcools.atprotocol.codegen.generator.objects;

import be.tomcools.atprotocol.codegen.lexicon.LexObject;
import be.tomcools.atprotocol.codegen.lexicon.LexiconDoc;
import java.util.Collections;
import java.util.List;

public record ObjectInDocument(LexiconDoc doc, String objectKey, LexObject lexObject) {
	public List<String> requiredProperties() {
		return lexObject.required() != null ? lexObject.required() : Collections.emptyList();
	}
};
