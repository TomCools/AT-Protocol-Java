package be.tomcools.atprotocol.codegen.generator.objects;

import static be.tomcools.atprotocol.codegen.generator.resolvers.NameResolver.*;
import static be.tomcools.atprotocol.codegen.generator.resolvers.TypeResolver.determineType;

import be.tomcools.atprotocol.codegen.ATProtocolCodeGenerator;
import be.tomcools.atprotocol.codegen.generator.resolvers.ClassNameDefinition;
import be.tomcools.atprotocol.codegen.generator.resolvers.NameResolver;
import be.tomcools.atprotocol.codegen.lexicon.LexType;
import com.squareup.javapoet.*;
import javax.annotation.processing.Generated;
import javax.lang.model.element.Modifier;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.stream.Collectors;

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

		// Default constructor
		classfile.addMethod(MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC).build());

		// All Args Constructor
		addAllArgsAndProperties(obj, classfile);

		// Builder Class
		addBuilder(classfile, nameDefinition.className(), obj);

		return JavaFile.builder(nameDefinition.packageName(), classfile.build()).build();
	}

	private void addAllArgsAndProperties(ObjectInDocument obj, TypeSpec.Builder classfile) {
		MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC);

		obj.lexObject().properties().entrySet().stream()
				.forEach((entry) -> {
					TypeName type = determineType(obj.doc().id(), entry.getValue());
					addProperty(classfile, type, entry.getKey(), entry.getValue());
					// create all args constructor
					addToConstructor(constructorBuilder, type, entry.getKey());
				});
		classfile.addMethod(constructorBuilder.build());
	}

	private void addBuilder(TypeSpec.Builder classfile, String className, ObjectInDocument obj) {
		String builderClassName = className + "Builder";
		TypeSpec.Builder builderClass = TypeSpec.classBuilder(builderClassName)
				.addModifiers(Modifier.PUBLIC, Modifier.STATIC);

		// From method with required fields
		MethodSpec.Builder staticFrom = MethodSpec.methodBuilder("from")
				.addModifiers(Modifier.STATIC, Modifier.PUBLIC)
				.returns(ClassName.bestGuess(builderClassName));
		staticFrom.addStatement("var builder = new " + builderClassName + "()");
		obj.lexObject().properties().entrySet().stream()
				.filter(p -> isRequiredField(obj, p))
				.forEach(entry -> {
					TypeName type = determineType(obj.doc().id(), entry.getValue());
					staticFrom.addParameter(type, entry.getKey());
					staticFrom.addStatement("builder." + entry.getKey() + "(" + entry.getKey() + ")");
				});
		staticFrom.addStatement("return builder");
		classfile.addMethod(staticFrom.build());

		// Builder method completelyEmptyBuilder
		MethodSpec.Builder staticBuilder = MethodSpec.methodBuilder("builder")
				.addModifiers(Modifier.STATIC, Modifier.PUBLIC)
				.returns(ClassName.bestGuess(builderClassName));
		staticBuilder.addStatement("return new " + builderClassName + "()");
		classfile.addMethod(staticBuilder.build());

		// builder methods for all fields
		obj.lexObject().properties().entrySet().stream()
				.forEach(entry -> {
					String propname = entry.getKey();
					TypeName type = determineType(obj.doc().id(), entry.getValue());

					builderClass.addField(FieldSpec.builder(type, propname).addModifiers(Modifier.PRIVATE).build());
					MethodSpec.Builder builderMethodForProperty = MethodSpec.methodBuilder(propname)
							.addParameter(type, propname, Modifier.FINAL)
							.addStatement("this.%s = %s".formatted(propname, propname))
							.addStatement("return this")
							.returns(ClassName.bestGuess(builderClassName))
							.addModifiers(Modifier.PUBLIC);
					builderClass.addMethod(builderMethodForProperty.build());
				});

		String paramatersForAllArgsConstructor = obj.lexObject().properties()
				.entrySet().stream()
				.map(Map.Entry::getKey)
				.collect(Collectors.joining(","));

		// fix constructor builder
		builderClass.addMethod(MethodSpec.methodBuilder("build")
				.addModifiers( Modifier.PUBLIC)
				.returns(ClassName.bestGuess(className))
				.addStatement("return new " + className + "(" + paramatersForAllArgsConstructor + ")").build());


		classfile.addType(builderClass.build());
	}

	private static boolean isRequiredField(ObjectInDocument obj, Map.Entry<String, LexType> p) {
		return obj.requiredProperties().contains(p.getKey());
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

		MethodSpec setter = MethodSpec.methodBuilder("set" + StringUtils.capitalize(propertyName))
				.addModifiers(Modifier.PUBLIC).addStatement("this. " + propertyName + " = " + propertyName).build();

		classfile.addField(fieldBuilder.build());
		classfile.addMethod(getter);
		classfile.addMethod(setter);
	}
}
