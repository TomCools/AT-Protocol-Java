package be.tomcools.atprotocol.codegen.maven;

import be.tomcools.atprotocol.codegen.ATPCodeGenConfiguration;
import be.tomcools.atprotocol.codegen.generator.ATProtocolCodeGeneratorImpl;
import java.nio.file.Path;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

@Mojo(name = "GenerateATProtocol", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class ATProtocolCodeGenMojo extends AbstractMojo {

	@Parameter(defaultValue = "${project}")
	private MavenProject project;

	@Parameter(required = false, defaultValue = "${project.basedir}/src/main/resources/lexicons/")
	private String sourcePath;

	@Parameter(required = false, defaultValue = "${project.build.directory}/generated-sources/java/")
	private String destinationPath;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		try {
			ATPCodeGenConfiguration configuration = ATPCodeGenConfiguration.builder()
					.withSource(Path.of(sourcePath).toFile()).withOutputDirectory(Path.of(destinationPath).toFile())
					.build();
			new ATProtocolCodeGeneratorImpl().generate(configuration);
		} catch (Exception e) {
			throw new MojoExecutionException("Failed to execute plugin", e);
		}

		// Make sure the compiler knows about the new files! ;)
		project.addCompileSourceRoot(destinationPath);
	}
}
