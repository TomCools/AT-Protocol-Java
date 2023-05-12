package be.tomcools.atprotocol.codegen.generator.procedure;

import be.tomcools.atprotocol.codegen.generator.GenerationContext;
import be.tomcools.atprotocol.codegen.generator.GenerationContextFile;
import be.tomcools.atprotocol.codegen.generator.resolvers.NameResolver;
import be.tomcools.atprotocol.codegen.lexicon.Schema;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import javax.lang.model.element.Modifier;

public class ProcedureMethodGenerator {

	private final NameResolver nameResolver;

	public ProcedureMethodGenerator(NameResolver nameResolver) {
		this.nameResolver = nameResolver;
	}

	public void addMethods(GenerationContext context, TypeSpec.Builder classfile) {
		for (GenerationContextFile file : context.files()) {
			file.getProcedures().forEach(p -> {
				addProcedure(classfile, p);
			});
		}
	}

	private void addProcedure(TypeSpec.Builder classfile, ProcedureInDocument procedure) {
		ClassName outputClass = determineOutputClass(procedure);

		MethodSpec method = MethodSpec.methodBuilder(nameResolver.resolveMethodName(procedure))
				.addException(ClassName.get(IOException.class)).addException(ClassName.get(InterruptedException.class))
				.addModifiers(Modifier.PUBLIC).addParameter(determineInputClass(procedure), "input")
				.returns(outputClass)
				.addStatement("//" + procedure.doc().id() + ": " + procedure.lexProcedure().description())
				.addCode("""
						     return executeRequest("POST", "/xrpc/%s", input, %s.class);
						""".formatted(procedure.doc().id(), outputClass.simpleName()), ClassName.get(HttpRequest.class),
						ClassName.get(URI.class), ClassName.get(Duration.class),
						ClassName.get(HttpRequest.BodyPublishers.class), ClassName.get(HttpResponse.BodyHandlers.class))
				.build();

		classfile.addMethod(method);
	}

	private ClassName determineInputClass(ProcedureInDocument procedure) {
		Schema schema = procedure.lexProcedure().input().schema();
		return this.nameResolver.determineClassNameForSchema(procedure.doc().id(), schema, "Input");
	}

	private ClassName determineOutputClass(ProcedureInDocument procedure) {
		Schema schema = procedure.lexProcedure().output().schema();
		return this.nameResolver.determineClassNameForSchema(procedure.doc().id(), schema, "Output");
	}
}
