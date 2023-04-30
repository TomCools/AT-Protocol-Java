package be.tomcools.atprotocol.codegen;

public interface ATProtocolCodeGenerator {
	// If it's a directory, read all files in that directory. Else it's a single
	// file.
	void generate(ATPCodeGenConfiguration configuration);
}
