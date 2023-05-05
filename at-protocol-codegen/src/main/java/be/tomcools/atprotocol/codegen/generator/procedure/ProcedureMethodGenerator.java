package be.tomcools.atprotocol.codegen.generator.procedure;

import be.tomcools.atprotocol.codegen.generator.GenerationContext;
import be.tomcools.atprotocol.codegen.generator.GenerationContextFile;
import be.tomcools.atprotocol.codegen.generator.nameresolvers.NameResolver;
import be.tomcools.atprotocol.codegen.lexicon.Schema;
import be.tomcools.atprotocol.codegen.lexicon.SchemaRef;
import com.squareup.javapoet.*;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;
import javax.lang.model.element.Modifier;
import org.apache.commons.lang3.StringUtils;

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
		MethodSpec method = MethodSpec.methodBuilder(nameResolver.resolveMethodName(procedure))
				.addModifiers(Modifier.PUBLIC)
				.addParameter(determineInputClass(procedure), "input").returns(determineOutputClass(procedure))
				.addStatement("//" + procedure.doc().id() + ": " + procedure.lexProcedure().description())
				.addCode("""
						var requestBuilder = $T.newBuilder()
						        .uri($T.create("https://foo.com/"))
						        .timeout($T.ofMinutes(2))
						        .header("Content-Type", "application/json")
						        .POST($T.ofString("TEST"));
						
						for (Map.Entry<String, String> requestHeader : requestHeaders.entrySet()) {
							requestBuilder.setHeader(requestHeader.getKey(), requestHeader.getValue());
						}
						
						var request = requestBuilder.build();
						// WIP return httpClient.send(request, $T.ofString());
						return null;
						""", ClassName.get(HttpRequest.class), ClassName.get(URI.class), ClassName.get(Duration.class),
						ClassName.get(HttpRequest.BodyPublishers.class), ClassName.get(HttpResponse.BodyHandlers.class))
				.build();

		classfile.addMethod(method);
	}

	private static ClassName determineInputClass(ProcedureInDocument procedure) {
		Schema schema = procedure.lexProcedure().input().schema();
		return determineClassName(procedure, schema, "Input");
	}

	private static ClassName determineOutputClass(ProcedureInDocument procedure) {
		Schema schema = procedure.lexProcedure().output().schema();
		return determineClassName(procedure, schema, "Output");
	}

	private static ClassName determineClassName(ProcedureInDocument procedure, Schema schema, String postFix) {
		String docFullName = procedure.doc().id().toString().toLowerCase();
		if (schema instanceof SchemaRef) {
			return ClassName.get(docFullName,
					StringUtils.capitalize(((SchemaRef) schema).getReference().replace("#", "")));
		} else {
			return ClassName.get(docFullName, StringUtils.capitalize(procedure.doc().id().getName() + postFix));
		}
	}
}
