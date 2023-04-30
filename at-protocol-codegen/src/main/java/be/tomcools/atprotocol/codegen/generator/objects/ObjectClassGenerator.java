package be.tomcools.atprotocol.codegen.generator.objects;

import be.tomcools.atprotocol.codegen.ATProtocolCodeGenerator;
import be.tomcools.atprotocol.codegen.errors.ATPCodeGenException;
import be.tomcools.atprotocol.codegen.lexicon.LexType;
import com.squareup.javapoet.*;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.processing.Generated;
import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;

import static be.tomcools.atprotocol.codegen.generator.TypeResolver.determineType;

public class ObjectClassGenerator {

	public void generateClassForObject(ObjectInDocument doc, File outputDirectory) {
		TypeSpec.Builder classfile = TypeSpec.classBuilder(doc.determineClassName()).addModifiers(Modifier.PUBLIC)
				.addAnnotation(AnnotationSpec.builder(Generated.class)
						.addMember("value", "$S", ATProtocolCodeGenerator.class.getName()).build());

		doc.obj().properties().forEach((k, v) -> {
			addProperty(classfile, doc, k, v);
		});

		JavaFile javaFile = JavaFile.builder(doc.determinePackageName(), classfile.build()).build();

		try {
			javaFile.writeTo(System.out);
			javaFile.writeTo(outputDirectory);
		} catch (IOException e) {
			throw new ATPCodeGenException("Couldn't write generated file to directory", e);
		}
	}

	private static void addProperty(TypeSpec.Builder classfile, ObjectInDocument doc, String propertyName, LexType v) {
		TypeName type = determineType(doc.doc().id(), v);

		FieldSpec.Builder fieldBuilder = FieldSpec.builder(type, propertyName).addModifiers(Modifier.PRIVATE);
		if (v.description() != null) {
			fieldBuilder.addJavadoc(v.description() + System.lineSeparator());
		}

		MethodSpec getter = MethodSpec.methodBuilder("get" + StringUtils.capitalize(propertyName)).returns(type)
				.addStatement("return " + propertyName).build();

		classfile.addField(fieldBuilder.build());
		classfile.addMethod(getter);
	}
}
