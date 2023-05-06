package be.tomcools.atprotocol.codegen.generator.query;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.lang.model.element.Modifier;

public class QueryMethodGenerator {

	private final NameResolver nameResolver;

	public QueryMethodGenerator(NameResolver nameResolver) {
		this.nameResolver = nameResolver;
	}

	public void addMethods(GenerationContext context, TypeSpec.Builder classfile) {
		for (GenerationContextFile file : context.files()) {
			file.getQueries().forEach(p -> {
				addMethods(classfile, p);
			});
		}
	}

	private void addMethods(TypeSpec.Builder classfile, QueryInDocument query) {
		ClassName outputClass = determineOutputClass(query);

		MethodSpec.Builder method = MethodSpec.methodBuilder(nameResolver.resolveMethodName(query))
				.addException(ClassName.get(IOException.class)).addException(ClassName.get(InterruptedException.class))
				.addModifiers(Modifier.PUBLIC).returns(outputClass);

		String requestParameters = handleParameters(query, method);

		method.addStatement("//" + query.doc().id() + ": " + query.lexQuery().description()).addCode("""
				     return executeRequest("GET", "/xrpc/%s%s", null, %s.class);
				""".formatted(query.doc().id(), requestParameters, outputClass.simpleName()),
				ClassName.get(HttpRequest.class), ClassName.get(URI.class), ClassName.get(Duration.class),
				ClassName.get(HttpResponse.BodyHandlers.class));

		classfile.addMethod(method.build());
	}

	private static String handleParameters(QueryInDocument query, MethodSpec.Builder method) {

		Set<Map.Entry<String, Object>> parameters = query.lexQuery().parameters().properties().entrySet();

		List<String> queryParam = new ArrayList<>();
		for (var entry : parameters) {
			String parameter = entry.getKey();
			method.addParameter(ClassName.get(String.class), parameter).addModifiers(Modifier.FINAL);
			queryParam.add(parameter + "=\" + " + parameter + " + \"");
		}

		return queryParam.stream().collect(Collectors.joining("&", "?", ""));
	}

	private ClassName determineOutputClass(QueryInDocument query) {
		Schema schema = query.lexQuery().output().schema();
		return this.nameResolver.determineClassNameForSchema(query.doc().id(), schema, "Output");
	}
}
