package be.tomcools.atprotocol.codegen.generator;

import be.tomcools.atprotocol.codegen.ATPCodeGenConfiguration;
import be.tomcools.atprotocol.codegen.ATProtocolCodeGenerator;
import be.tomcools.atprotocol.codegen.errors.ATPCodeGenException;
import be.tomcools.atprotocol.codegen.parser.LexiconParser;
import be.tomcools.atprotocol.codegen.tools.ContextValidator;
import com.github.wnameless.json.flattener.JsonFlattener;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;

public class ATProtocolCodeGeneratorImpl implements ATProtocolCodeGenerator {
	private final LexiconParser parser = new LexiconParser();
	private final ContextValidator validator = new ContextValidator();

	@Override
	public void generate(ATPCodeGenConfiguration configuration) {
		List<GenerationContextFile> docs = readFiles(configuration.getSource());

		GenerationContext context = new GenerationContext(docs, configuration.getOutputDirectory());
		validator.validate(context);

		CodeGen gen = new CodeGen(context);
		gen.generateClasses();
	}

	private List<GenerationContextFile> readFiles(File source) {
		try {
			if (source.isDirectory()) {
				return Files.list(source.toPath()).map(file -> createContextForFile(file.toFile())).toList();
			} else {
				return Collections.singletonList(createContextForFile(source));
			}
		} catch (IOException e) {
			throw new ATPCodeGenException("Error while reading input files", e);
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
			throw new ATPCodeGenException("Error while reading input files", e);
		}
	}
}
