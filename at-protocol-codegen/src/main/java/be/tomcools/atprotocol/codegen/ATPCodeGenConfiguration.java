package be.tomcools.atprotocol.codegen;

import java.io.File;

public class ATPCodeGenConfiguration {
	private final File source;
	private final File outputDirectory;

	private ATPCodeGenConfiguration(File source, File outputDirectory) {
		this.source = source;
		this.outputDirectory = outputDirectory;
	}

	public File getSource() {
		return source;
	}

	public File getOutputDirectory() {
		return outputDirectory;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private File source;
		private File outputDirectory;

		public Builder withSource(File source) {
			this.source = source;
			return this;
		}

		public Builder withOutputDirectory(File outputDirectory) {
			this.outputDirectory = outputDirectory;
			return this;
		}

		public ATPCodeGenConfiguration build() {
			return new ATPCodeGenConfiguration(source, outputDirectory);
		}
	}
}
