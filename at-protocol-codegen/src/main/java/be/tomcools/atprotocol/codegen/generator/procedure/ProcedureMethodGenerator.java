package be.tomcools.atprotocol.codegen.generator.procedure;

import be.tomcools.atprotocol.codegen.generator.GenerationContext;
import be.tomcools.atprotocol.codegen.generator.GenerationContextFile;
import be.tomcools.atprotocol.codegen.lexicon.Schema;
import be.tomcools.atprotocol.codegen.lexicon.SchemaRef;
import com.squareup.javapoet.*;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import javax.lang.model.element.Modifier;
import org.apache.commons.lang3.StringUtils;

public class ProcedureMethodGenerator {

	public void addMethods(GenerationContext context, TypeSpec.Builder classfile) {
		for (GenerationContextFile file : context.files()) {
			file.getProcedures().forEach(p -> addProcedure(classfile, p));
		}
	}

	private void addProcedure(TypeSpec.Builder classfile, ProcedureInDocument procedure) {
		MethodSpec method = MethodSpec.methodBuilder(procedure.determineMethodName()).addModifiers(Modifier.PUBLIC)
				.addParameter(determineInputClass(procedure), "input").returns(determineOutputClass(procedure))
				.addStatement("//" + procedure.doc().id() + ": " + procedure.obj().description())
				.addCode("""
						var request = $T.newBuilder()
						        .uri($T.create("https://foo.com/"))
						        .timeout($T.ofMinutes(2))
						        .header("Content-Type", "application/json")
						        .POST($T.ofString("TEST"))
						        .build();
						// WIP return httpClient.send(request, $T.ofString());
						return null;
						""", ClassName.get(HttpRequest.class), ClassName.get(URI.class), ClassName.get(Duration.class),
						ClassName.get(HttpRequest.BodyPublishers.class), ClassName.get(HttpResponse.BodyHandlers.class))
				.build();

		classfile.addMethod(method);
	}

	private static ClassName determineInputClass(ProcedureInDocument procedure) {
		Schema schema = procedure.obj().input().schema();
		String procedureName = procedure.name().toLowerCase();
		if (schema instanceof SchemaRef) {
			return ClassName.get(procedureName,
					StringUtils.capitalize(((SchemaRef) schema).getReference().replace("#", "")));
		} else {
			return ClassName.get(procedureName, StringUtils.capitalize(procedure.doc().id().getName() + "Input"));
		}
	}

	private static ClassName determineOutputClass(ProcedureInDocument procedure) {
		Schema schema = procedure.obj().output().schema();
		String procedureName = procedure.name().toLowerCase();
		if (schema instanceof SchemaRef) {
			return ClassName.get(procedureName,
					StringUtils.capitalize(((SchemaRef) schema).getReference().replace("#", "")));
		} else {
			return ClassName.get(procedureName, StringUtils.capitalize(procedure.doc().id().getName() + "Output"));
		}
	}
}
