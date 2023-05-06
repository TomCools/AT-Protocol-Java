package be.tomcools.atprotocol.codegen.generator.objects;

import static be.tomcools.atprotocol.codegen.generator.resolvers.TypeResolver.determineType;

import be.tomcools.atprotocol.codegen.ATProtocolCodeGenerator;
import be.tomcools.atprotocol.codegen.generator.resolvers.ClassNameDefinition;
import be.tomcools.atprotocol.codegen.generator.resolvers.NameResolver;
import be.tomcools.atprotocol.codegen.lexicon.LexType;
import com.squareup.javapoet.*;
import javax.annotation.processing.Generated;
import javax.lang.model.element.Modifier;
import org.apache.commons.lang3.StringUtils;

public class ObjectClassGenerator {

	private final NameResolver nameResolver;

	public ObjectClassGenerator(NameResolver nameResolver) {
		this.nameResolver = nameResolver;
	}

	public JavaFile generateClassForObject(ObjectInDocument obj) {
		ClassNameDefinition nameDefinition = nameResolver.resolve(obj);

		TypeSpec.Builder classfile = TypeSpec.classBuilder(nameDefinition.className()).addModifiers(Modifier.PUBLIC)
				.addAnnotation(AnnotationSpec.builder(Generated.class)
						.addMember("value", "$S", ATProtocolCodeGenerator.class.getName()).build());

		MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC);

		// Only generating required properties for now.
		obj.lexObject().properties().entrySet().stream().filter(p -> obj.requiredProperties().contains(p.getKey()))
				.forEach((entry) -> {
					TypeName type = determineType(obj.doc().id(), entry.getValue());
					addProperty(classfile, type, entry.getKey(), entry.getValue());

					// create all args constructor
					addToConstructor(constructorBuilder, type, entry.getKey());
				});

		classfile.addMethod(constructorBuilder.build());

		return JavaFile.builder(nameDefinition.packageName(), classfile.build()).build();
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
