package be.tomcools.atprotocol.codegen.generator;

import static be.tomcools.atprotocol.codegen.generator.resolvers.NameResolver.*;

import be.tomcools.atprotocol.codegen.ATProtocolCodeGenerator;
import be.tomcools.atprotocol.codegen.errors.ATPCodeGenException;
import be.tomcools.atprotocol.codegen.generator.objects.ObjectClassGenerator;
import be.tomcools.atprotocol.codegen.generator.procedure.ProcedureMethodGenerator;
import be.tomcools.atprotocol.codegen.generator.query.QueryMethodGenerator;
import com.squareup.javapoet.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.processing.Generated;
import javax.lang.model.element.Modifier;

public class CodeGen {

	GenerationContext context;
	ObjectClassGenerator objectClassGenerator;
	ProcedureMethodGenerator procedureMethodGenerator;
	QueryMethodGenerator queryMethodGenerator;
	BaseGenerators baseGenerator;

	public CodeGen(GenerationContext context) {
		this.context = context;
		this.objectClassGenerator = new ObjectClassGenerator(context.nameResolver());
		this.queryMethodGenerator = new QueryMethodGenerator(context.nameResolver());
		this.procedureMethodGenerator = new ProcedureMethodGenerator(context.nameResolver());
		this.baseGenerator = new BaseGenerators();
	}

	public void generateClasses() {
		generateObjects();

		generateClient();

		generateJsonBodyHandlerInterface();

		generateExceptionClass();
	}

	private void generateExceptionClass() {
		this.writeFile(baseGenerator.generateException(CLIENT_EXCEPTION_CLASS_NAME));
	}

	// All POJOs are generated in separate Java files.
	private void generateObjects() {
		context.files().forEach(c -> {
			c.getObjects().forEach(object -> {
				JavaFile file = objectClassGenerator.generateClassForObject(object);
				this.writeFile(file);
			});
		});
	}

	// All Queries and Procedures are generated in the same Java file.
	private void generateClient() {
		TypeSpec.Builder classfile = TypeSpec.classBuilder(CLIENT_CLASS_NAME).addModifiers(Modifier.PUBLIC)
				.addAnnotation(AnnotationSpec.builder(Generated.class)
						.addMember("value", "$S", ATProtocolCodeGenerator.class.getName()).build())
				.addMethod(MethodSpec.constructorBuilder().build());

		addHttpClient(classfile);
		addStaticInitializer(classfile);

		procedureMethodGenerator.addMethods(context, classfile);
		queryMethodGenerator.addMethods(context, classfile);

		JavaFile javaFile = JavaFile.builder(FRAMEWORK_PACKAGE_NAME, classfile.build()).build();

		writeFile(javaFile);
	}

	private void writeFile(JavaFile javaFile) {
		try {
			javaFile.writeTo(context.outputDirectory());
		} catch (IOException e) {
			throw new ATPCodeGenException("Couldn't write generated file to directory", e);
		}
	}

	private void generateJsonBodyHandlerInterface() {
		TypeSpec.Builder classfile = TypeSpec.interfaceBuilder(JSON_HANDLER_CLASS_NAME).addModifiers(Modifier.PUBLIC)
				.addAnnotation(AnnotationSpec.builder(Generated.class)
						.addMember("value", "$S", ATProtocolCodeGenerator.class.getName()).build());

		TypeVariableName typeVariableName = TypeVariableName.get("T");
		ParameterizedTypeName classType = ParameterizedTypeName.get(ClassName.get(Class.class), typeVariableName);

		classfile.addMethod(MethodSpec.methodBuilder("fromJson").addModifiers(Modifier.ABSTRACT, Modifier.PUBLIC)
				.addTypeVariable(typeVariableName).addParameter(ClassName.get(String.class), "input")
				.addParameter(classType, "targetClass").returns(typeVariableName).build());
		classfile.addMethod(MethodSpec.methodBuilder("toJson").addModifiers(Modifier.ABSTRACT, Modifier.PUBLIC)
				.addTypeVariable(typeVariableName).addParameter(typeVariableName, "input")
				.returns(ClassName.get(String.class)).build());

		JavaFile javaFile = JavaFile.builder(FRAMEWORK_PACKAGE_NAME, classfile.build()).build();

		writeFile(javaFile);
	}

	private static void addStaticInitializer(TypeSpec.Builder classfile) {
		String baseUrl = "baseUrl";
		ClassName baseUrlType = ClassName.get(String.class);
		String mapper = "mapper";
		ClassName mapperType = ClassName.get(FRAMEWORK_PACKAGE_NAME, JSON_HANDLER_CLASS_NAME);

		classfile.addField(FieldSpec.builder(baseUrlType, baseUrl).addModifiers(Modifier.PRIVATE).build());
		classfile.addField(FieldSpec.builder(mapperType, mapper).addModifiers(Modifier.PRIVATE).build());

		classfile.addMethod(MethodSpec.constructorBuilder().addParameter(baseUrlType, baseUrl, Modifier.FINAL)
				.addParameter(mapperType, mapper, Modifier.FINAL)
				.addStatement("this.%s = %s".formatted(baseUrl, baseUrl))
				.addStatement("this.%s = %s".formatted(mapper, mapper)).addModifiers(Modifier.PRIVATE).build());

		classfile.addMethod(MethodSpec.methodBuilder("service").addModifiers(Modifier.STATIC, Modifier.PUBLIC)
				.addParameter(baseUrlType, baseUrl).addParameter(mapperType, mapper)
				.returns(ClassName.bestGuess(CLIENT_CLASS_NAME))
				.addStatement("return new AtpApi(" + baseUrl + "," + mapper + ")").build());
	}

	private static void addHttpClient(TypeSpec.Builder classfile) {
		classfile.addField(FieldSpec.builder(ClassName.get(HttpClient.class), "httpClient")
				.addModifiers(Modifier.PRIVATE).initializer("HttpClient.newHttpClient()").build());
		classfile.addField(FieldSpec
				.builder(ParameterizedTypeName.get(Map.class, String.class, String.class), "requestHeaders")
				.addModifiers(Modifier.PRIVATE)
				.initializer(CodeBlock.builder().add("new $T<>()", ClassName.get(HashMap.class)).build()).build());

		classfile.addMethod(MethodSpec.methodBuilder("setHeader").addModifiers(Modifier.PUBLIC)
				.addParameter(ClassName.get(String.class), "key").addParameter(ClassName.get(String.class), "value")
				.addStatement("this.requestHeaders.put(key, value)").build());

		addSharedExecutionMethod(classfile);
	}

	private static void addSharedExecutionMethod(TypeSpec.Builder classfile) {
		TypeVariableName typeVariableName = TypeVariableName.get("T");
		ParameterizedTypeName classType = ParameterizedTypeName.get(ClassName.get(Class.class), typeVariableName);

		MethodSpec.Builder clientMethod = MethodSpec.methodBuilder("executeRequest").addTypeVariable(typeVariableName)
				.addParameter(TypeName.get(String.class), "httpMethod").addParameter(TypeName.get(String.class), "url")
				.addParameter(TypeName.get(Object.class), "body").addParameter(classType, "responseClass")
				.addModifiers(Modifier.PRIVATE).returns(typeVariableName);

		clientMethod.addCode(
				"""
						               try {
						                       var jsonRequest = mapper.toJson(body);
						                        var requestBuilder = $T.newBuilder()
						                       .uri($T.create(baseUrl + url))
						                       .timeout($T.ofMinutes(2))
						                       .header("Content-Type", "application/json");

						                       if(httpMethod.equals("POST")) {
						                        requestBuilder.POST($T.ofString(jsonRequest));
						                       } else if(httpMethod.equals("GET")) {
						                        requestBuilder.GET();
						                       } else {
						                        throw new AtpApiException("Invalid method provided: " + httpMethod);
						                       }
						                        for (Map.Entry<String, String> requestHeader : requestHeaders.entrySet()) {
						                            requestBuilder.setHeader(requestHeader.getKey(), requestHeader.getValue());
						                        }

						                        var request = requestBuilder.build();

						                        var jsonResponse = httpClient.send(request, $T.ofString());

						                           int statusCode = jsonResponse.statusCode();
						                           if(!String.valueOf(statusCode).startsWith("2")) {
						                               throw new AtpApiException("Unexpected HTTP Status Code received: " + statusCode);
						                           }

						                        return mapper.fromJson(jsonResponse.body(), responseClass);
						                 }
						                catch(Exception ex) {
						                    throw new AtpApiException("Error while sending HTTP request", ex);
						                }
						"""
						.trim(),
				ClassName.get(HttpRequest.class), ClassName.get(URI.class), ClassName.get(Duration.class),
				ClassName.get(HttpRequest.BodyPublishers.class), ClassName.get(HttpResponse.BodyHandlers.class));

		classfile.addMethod(clientMethod.build());
	}
}
