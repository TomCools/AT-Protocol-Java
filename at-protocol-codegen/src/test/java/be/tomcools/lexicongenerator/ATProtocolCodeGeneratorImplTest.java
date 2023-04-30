package be.tomcools.lexicongenerator;

import be.tomcools.atprotocol.codegen.ATProtocolCodeGeneratorImpl;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.tools.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ATProtocolCodeGeneratorImplTest {

	@TempDir
	File tempDir;

	ATProtocolCodeGeneratorImpl sut = new ATProtocolCodeGeneratorImpl();

	@Test
	public void testParserWithAllLexiconFiles() throws IOException {
		sut.generate(lexiconFileTestDirectory(), tempDir);

		testCompilation();
	}

	@Test
	public void createClassesForSimpleDefinition() throws IOException {
		sut.generate(Paths.get("src", "test", "resources", "lexicons", "com.atproto.label.defs.json").toFile(),
				tempDir);

		testCompilation();
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

	public static File lexiconFileTestDirectory() throws IOException {
		return Paths.get("src", "test", "resources", "lexicons").toFile();
	}
}
