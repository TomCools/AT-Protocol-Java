package be.tomcools.atprotocol.codegen;

import be.tomcools.atprotocol.codegen.api.ATProtocolCodeGenerator;
import be.tomcools.atprotocol.codegen.generator.CodeGen;
import be.tomcools.atprotocol.codegen.lexicon.NSID;
import be.tomcools.atprotocol.codegen.parser.LexiconParser;
import com.github.wnameless.json.flattener.JsonFlattener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ATProtocolCodeGeneratorImpl implements ATProtocolCodeGenerator {
	private LexiconParser parser = new LexiconParser();

	// If it's a directory, read all files in that directory. Else it's a single
	// file.
	public void generate(File source, File outputDirectory) {
		List<GenerationContextFile> docs = readFiles(source);

		GenerationContext context = new GenerationContext(docs, outputDirectory);
		validateReferences(context);

		CodeGen gen = new CodeGen(context);
		gen.generateClasses();
	}

	private List<GenerationContextFile> readFiles(File source) {
		try {
			if (source.isDirectory()) {
				return Files.list(source.toPath()).map(file -> {
					;
					return createContextForFile(file.toFile());
				}).toList();
			} else {
				return Collections.singletonList(createContextForFile(source));
			}

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private GenerationContextFile createContextForFile(File f) {
		String jsonFile = readFile(f);
		return new GenerationContextFile(parser.fromJson(jsonFile), JsonFlattener.flattenAsMap(jsonFile));
	}

	private String readFile(File f) {
		try {
			return Files.readString(f.toPath());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
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
					context.getForId(new NSID(referenceEntityValue))
							.orElseThrow(() -> new IllegalStateException("Identifier does not exist"));
				}
			});
		});
	}

	private void validateInDocumentReference(GenerationContextFile e, String referenceEntity) {
		String expectedReference = "defs." + referenceEntity.replace("#", "") + ".type";
		if (!e.flatJson().containsKey(expectedReference)) {
			throw new IllegalArgumentException(expectedReference + " does not exist in document " + e.doc().id());
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
