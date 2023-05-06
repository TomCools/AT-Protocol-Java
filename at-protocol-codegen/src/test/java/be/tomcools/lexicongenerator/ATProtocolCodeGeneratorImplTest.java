package be.tomcools.lexicongenerator;

import be.tomcools.atprotocol.codegen.ATPCodeGenConfiguration;
import be.tomcools.atprotocol.codegen.errors.ATPCodeGenException;
import be.tomcools.atprotocol.codegen.generator.ATProtocolCodeGeneratorImpl;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.tools.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ATProtocolCodeGeneratorImplTest {

	@TempDir
	File tempDir;

	ATProtocolCodeGeneratorImpl sut = new ATProtocolCodeGeneratorImpl();

	@Test
	public void givenDirectoryWithCompatibleLexicons_afterCreatingJavaSources_shouldCompileSuccessfully()
			throws IOException {
		ATPCodeGenConfiguration configuration = ATPCodeGenConfiguration.builder().withSource(lexiconFileTestDirectory())
				.withOutputDirectory(tempDir).build();

		sut.generate(configuration);

		testCompilation();
	}

	@Test
	public void givenLexiconWithSingleObjectDeclarationInMain_afterCreatingJavaSources_shouldCompileSuccessfully()
			throws IOException {
		var singleFile = Paths.get("src", "test", "resources", "lexicons", "com.atproto.repo.strongRef.json").toFile();

		ATPCodeGenConfiguration configuration = ATPCodeGenConfiguration.builder().withSource(singleFile)
				.withOutputDirectory(tempDir).build();

		sut.generate(configuration);

		testCompilation();
	}

	@Test
	public void givenLexiconWithSingleObjectDeclaration_afterCreatingJavaSources_shouldCompileSuccessfully()
			throws IOException {
		var singleFile = Paths.get("src", "test", "resources", "lexicons", "com.atproto.label.defs.json").toFile();

		ATPCodeGenConfiguration configuration = ATPCodeGenConfiguration.builder().withSource(singleFile)
				.withOutputDirectory(tempDir).build();

		sut.generate(configuration);

		testCompilation();
	}

	@Test
	public void givenLexiconWithQueryDeclaration_afterCreatingJavaSources_shouldCompileSuccessfully()
			throws IOException {
		var singleFile = Paths.get("src", "test", "resources", "lexicons", "com.atproto.identity.resolveHandle.json")
				.toFile();

		ATPCodeGenConfiguration configuration = ATPCodeGenConfiguration.builder().withSource(singleFile)
				.withOutputDirectory(tempDir).build();

		sut.generate(configuration);

		testCompilation();
	}

	@Test
	public void givenLexiconWithProcedureDeclaration_afterCreatingJavaSources_shouldCompileSuccessfully()
			throws IOException {
		var singleFile = Paths.get("src", "test", "resources", "lexicons", "com.atproto.server.createSession.json")
				.toFile();

		ATPCodeGenConfiguration configuration = ATPCodeGenConfiguration.builder().withSource(singleFile)
				.withOutputDirectory(tempDir).build();

		sut.generate(configuration);

		testCompilation();
	}

	@Test
	public void givenLexiconWithProcedureDeclarationReferenced_afterCreatingJavaSources_shouldCompileSuccessfully()
			throws IOException {
		var singleFile = Paths.get("src", "test", "resources", "lexicons", "com.atproto.server.createAccount.json")
				.toFile();

		ATPCodeGenConfiguration configuration = ATPCodeGenConfiguration.builder().withSource(singleFile)
				.withOutputDirectory(tempDir).build();

		sut.generate(configuration);

		testCompilation();
	}

	@Test
	public void givenLexiconWithFaultyReference_shouldFailValidation() throws IOException {
		var faultyRefPath = Paths.get("src", "test", "resources", "faultyref").toFile();

		ATPCodeGenConfiguration configuration = ATPCodeGenConfiguration.builder().withSource(faultyRefPath)
				.withOutputDirectory(tempDir).build();

		Assertions.assertThrows(ATPCodeGenException.class, () -> {
			sut.generate(configuration);
		});
	}

	private void testCompilation() throws IOException {
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);

		Path[] generatedFilepath = Files.walk(tempDir.toPath()).filter(p -> p.toFile().isFile()).toArray(Path[]::new);

		Iterable<? extends JavaFileObject> fileObjects = fileManager.getJavaFileObjects(generatedFilepath);
		DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
		compiler.getTask(null, fileManager, diagnostics, null, null, fileObjects).call();
		for (var diagnostic : diagnostics.getDiagnostics()) {
			if (diagnostic.getKind() == Diagnostic.Kind.ERROR) {
				throw new RuntimeException(diagnostic.toString());
			}
		}
	}

	public static File lexiconFileTestDirectory() {
		return Paths.get("src", "test", "resources", "lexicons").toFile();
	}
}
