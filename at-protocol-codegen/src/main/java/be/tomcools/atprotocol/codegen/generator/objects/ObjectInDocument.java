package be.tomcools.atprotocol.codegen.generator.objects;

import be.tomcools.atprotocol.codegen.lexicon.LexObject;
import be.tomcools.atprotocol.codegen.lexicon.LexiconDoc;
import be.tomcools.atprotocol.codegen.lexicon.NSID;
import org.apache.commons.lang3.StringUtils;

public record ObjectInDocument(LexiconDoc doc, String objectKey, LexObject obj) {

	/**
	 * This method will return the expected package objectKey. Normally this is the
	 * document id. However, if the class objectKey is also the objectKey of the
	 * document, just use the domain.
	 *
	 * @return the expected package objectKey
	 */
	public String determinePackageName() {
		NSID identifier = doc.id();
		if (identifier.getName().equalsIgnoreCase(determineClassName())) {
			return identifier.getDomain().toLowerCase();
		} else {
			return identifier.toString().toLowerCase();
		}
	}

	/**
	 * @return the expected class objectKey;
	 */
	public String determineClassName() {
		// schema should not be taken into account for the objectKey, so remove it.
		String[] split = objectKey.replace(".schema", "").split("\\.");
		if (objectKey.contains(".schema")) {
			// return special object name with schema
			return StringUtils.capitalize(doc.id().getName()) + StringUtils.capitalize(split[split.length - 1]);
		} else {
			// return regular object name
			return StringUtils.capitalize(split[split.length - 1]);
		}
	}
};
