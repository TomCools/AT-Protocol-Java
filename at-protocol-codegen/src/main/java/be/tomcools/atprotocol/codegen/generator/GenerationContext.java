package be.tomcools.atprotocol.codegen.generator;

import be.tomcools.atprotocol.codegen.lexicon.NSID;
import java.io.File;
import java.util.List;
import java.util.Optional;

public record GenerationContext(List<GenerationContextFile> files, File outputDirectory) {

	public Optional<GenerationContextFile> getForId(NSID id) {
		return files.stream().filter(e -> e.doc().id().equals(id)).findFirst();
	}
}
