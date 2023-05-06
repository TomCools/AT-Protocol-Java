package be.tomcools.atprotocol.codegen.generator;

import static be.tomcools.atprotocol.codegen.generator.resolvers.NameResolver.FRAMEWORK_PACKAGE_NAME;

import be.tomcools.atprotocol.codegen.ATProtocolCodeGenerator;
import com.squareup.javapoet.*;
import javax.annotation.processing.Generated;
import javax.lang.model.element.Modifier;

public class BaseGenerators {

	public JavaFile generateException(String className) {

		TypeSpec.Builder classfile = TypeSpec.classBuilder(className)
				.addAnnotation(AnnotationSpec.builder(Generated.class)
						.addMember("value", "$S", ATProtocolCodeGenerator.class.getName()).build())
				.superclass(ClassName.get(RuntimeException.class)).addModifiers(Modifier.PUBLIC);

		classfile.addMethod(MethodSpec.constructorBuilder().addParameter(ClassName.get(String.class), "message")
				.addModifiers(Modifier.PUBLIC).addStatement("super(message);").build());

		classfile.addMethod(MethodSpec.constructorBuilder().addParameter(ClassName.get(String.class), "message")
				.addParameter(ClassName.get(Exception.class), "ex").addModifiers(Modifier.PUBLIC)
				.addStatement("super(message,ex);").build());

		return JavaFile.builder(FRAMEWORK_PACKAGE_NAME, classfile.build()).build();
	}
}
