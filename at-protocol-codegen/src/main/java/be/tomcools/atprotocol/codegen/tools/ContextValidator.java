package be.tomcools.atprotocol.codegen.tools;

import be.tomcools.atprotocol.codegen.errors.ATPCodeGenException;
import be.tomcools.atprotocol.codegen.generator.GenerationContext;
import be.tomcools.atprotocol.codegen.generator.GenerationContextFile;
import be.tomcools.atprotocol.codegen.lexicon.NSID;
import java.util.Map;

public class ContextValidator {

	public void validate(GenerationContext context) {
		this.validateReferences(context);
	}
	private void validateReferences(GenerationContext context) {
		// where-ever a references is pointed out, it should exist.
		context.files().forEach(e -> {
			e.flatJson().entrySet().stream().filter(this::getAllWithReference).forEach(referenceEntity -> {
				String referenceEntityValue = (String) referenceEntity.getValue();
				// refers to the current document
				if (referenceEntityValue.startsWith("#")) {
					validateInDocumentReference(e, referenceEntityValue);
				} else if (referenceEntityValue.contains("#")) {
					validateExternalReference(context, referenceEntityValue);
				} else {
					// edge case, where the entire other document references is an Object
					context.getForId(new NSID(referenceEntityValue)).orElseThrow(
							() -> new ATPCodeGenException("Referenced identifier does not exist in provided context: '"
									+ referenceEntityValue + "'"));
				}
			});
		});
	}

	private void validateInDocumentReference(GenerationContextFile e, String referenceEntity) {
		String expectedReference = "defs." + referenceEntity.replace("#", "") + ".type";
		if (!e.flatJson().containsKey(expectedReference)) {
			throw new ATPCodeGenException("Schema Validation Error: %s does not exist in document %s"
					.formatted(expectedReference, e.doc().id()));
		}
	}

	private void validateExternalReference(GenerationContext e, String referenceEntity) {
		String[] expectedReference = referenceEntity.split("#");

		// first part contains the reference to another document.
		GenerationContextFile file = e.getForId(new NSID(expectedReference[0]))
				.orElseThrow(() -> new RuntimeException("Could not find document for id " + referenceEntity));
		String identifier = expectedReference[1];

		this.validateInDocumentReference(file, identifier);
	}

	private boolean getAllWithReference(Map.Entry<String, Object> e) {
		return e.getValue().toString().contains("#");
	}
}
