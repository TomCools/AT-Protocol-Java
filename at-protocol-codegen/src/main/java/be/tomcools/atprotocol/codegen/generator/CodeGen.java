package be.tomcools.atprotocol.codegen.generator;

import be.tomcools.atprotocol.codegen.ATProtocolCodeGenerator;
import be.tomcools.atprotocol.codegen.errors.ATPCodeGenException;
import be.tomcools.atprotocol.codegen.generator.objects.ObjectClassGenerator;
import be.tomcools.atprotocol.codegen.generator.procedure.ProcedureMethodGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.javapoet.*;
import java.io.IOException;
import javax.annotation.processing.Generated;
import javax.lang.model.element.Modifier;

public class CodeGen {

	GenerationContext context;
	ObjectClassGenerator objectClassGenerator;
	ProcedureMethodGenerator procedureMethodGenerator;

	public CodeGen(GenerationContext context) {
		this.context = context;
		this.objectClassGenerator = new ObjectClassGenerator();
		this.procedureMethodGenerator = new ProcedureMethodGenerator();
	}

	public void generateClasses() {
		context.files().forEach(c -> {
			c.getObjects()
					.forEach(object -> objectClassGenerator.generateClassForObject(object, context.outputDirectory()));
		});

		generateClient();
	}

	private void generateClient() {

		TypeSpec.Builder classfile = TypeSpec.classBuilder("AtpApi").addModifiers(Modifier.PUBLIC)
				.addAnnotation(AnnotationSpec.builder(Generated.class)
						.addMember("value", "$S", ATProtocolCodeGenerator.class.getName()).build());

		addHttpClient(classfile);
		// WIP addObjectMapper(classfile);

		procedureMethodGenerator.addMethods(context, classfile);

		JavaFile javaFile = JavaFile.builder("be.tomcools.atprotocol.client", classfile.build()).build();

		try {
			javaFile.writeTo(System.out);
			javaFile.writeTo(context.outputDirectory());
		} catch (IOException e) {
			throw new ATPCodeGenException("Couldn't write generated file to directory", e);
		}
	}

	private void addObjectMapper(TypeSpec.Builder classfile) {
		classfile.addField(FieldSpec.builder(ClassName.get(ObjectMapper.class), "mapper").addModifiers(Modifier.PRIVATE)
				.initializer("new ObjectMapper()").build());
	}

	private static void addHttpClient(TypeSpec.Builder classfile) {
		classfile.addField(FieldSpec.builder(ClassName.get("java.net.http", "HttpClient"), "httpClient")
				.addModifiers(Modifier.PRIVATE).initializer("HttpClient.newHttpClient()").build());
	}
}
