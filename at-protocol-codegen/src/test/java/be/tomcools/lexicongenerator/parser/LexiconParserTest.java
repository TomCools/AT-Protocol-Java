package be.tomcools.lexicongenerator.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import be.tomcools.atprotocol.codegen.lexicon.LexiconDoc;
import be.tomcools.atprotocol.codegen.parser.LexiconParser;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class LexiconParserTest {
	LexiconParser sut = new LexiconParser();

	@ParameterizedTest
	@MethodSource("allLexiconFiles")
	public void testParserWithAllLexiconFiles(Path p) throws IOException {
		String json = Files.readString(p);

		LexiconDoc doc = sut.fromJson(json);
		String resultJson = sut.toJson(doc);

		assertEquals(sut.mapper.readTree(json), sut.mapper.readTree(resultJson));
	}

	public static Stream<Path> allLexiconFiles() throws IOException {
		return Files.list(Paths.get("src", "test", "resources", "lexicons"));
	}
}
