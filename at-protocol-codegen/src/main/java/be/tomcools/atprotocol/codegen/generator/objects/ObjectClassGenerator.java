package be.tomcools.atprotocol.codegen.generator.objects;

import static be.tomcools.atprotocol.codegen.generator.TypeResolver.determineType;

import be.tomcools.atprotocol.codegen.ATProtocolCodeGenerator;
import be.tomcools.atprotocol.codegen.errors.ATPCodeGenException;
import be.tomcools.atprotocol.codegen.lexicon.LexType;
import com.squareup.javapoet.*;
import java.io.File;
import java.io.IOException;
import javax.annotation.processing.Generated;
import javax.lang.model.element.Modifier;
import org.apache.commons.lang3.StringUtils;

public class ObjectClassGenerator {

	public void generateClassForObject(ObjectInDocument doc, File outputDirectory) {
		TypeSpec.Builder classfile = TypeSpec.classBuilder(doc.determineClassName()).addModifiers(Modifier.PUBLIC)
				.addAnnotation(AnnotationSpec.builder(Generated.class)
						.addMember("value", "$S", ATProtocolCodeGenerator.class.getName()).build());

		MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC);

		doc.obj().properties().forEach((k, v) -> {
			TypeName type = determineType(doc.doc().id(), v);
			addProperty(classfile, type, k, v);

			// create all args constructor
			addToConstructor(constructorBuilder, type, k);
		});

		classfile.addMethod(constructorBuilder.build());

		JavaFile javaFile = JavaFile.builder(doc.determinePackageName(), classfile.build()).build();

		try {
			javaFile.writeTo(System.out);
			javaFile.writeTo(outputDirectory);
		} catch (IOException e) {
			throw new ATPCodeGenException("Couldn't write generated file to directory", e);
		}
	}

	private void addToConstructor(MethodSpec.Builder constructorBuilder, TypeName type, String propertyName) {
		constructorBuilder.addParameter(type, propertyName, Modifier.FINAL);
		constructorBuilder.addStatement("this.%s = %s".formatted(propertyName, propertyName));
	}

	private static void addProperty(TypeSpec.Builder classfile, TypeName type, String propertyName, LexType v) {

		FieldSpec.Builder fieldBuilder = FieldSpec.builder(type, propertyName).addModifiers(Modifier.PRIVATE);
		if (v.description() != null) {
			fieldBuilder.addJavadoc(v.description() + System.lineSeparator());
		}

		MethodSpec getter = MethodSpec.methodBuilder("get" + StringUtils.capitalize(propertyName)).returns(type)
				.addModifiers(Modifier.PUBLIC).addStatement("return " + propertyName).build();

		classfile.addField(fieldBuilder.build());
		classfile.addMethod(getter);
	}
}
