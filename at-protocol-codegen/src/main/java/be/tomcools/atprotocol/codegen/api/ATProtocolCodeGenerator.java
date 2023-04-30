package be.tomcools.atprotocol.codegen.api;

import java.io.File;

public interface ATProtocolCodeGenerator {
	// If it's a directory, read all files in that directory. Else it's a single
	// file.
	void generate(File source, File outputDirectory);
}
