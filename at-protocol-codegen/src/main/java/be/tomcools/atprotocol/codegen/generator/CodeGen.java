package be.tomcools.atprotocol.codegen.generator;

import be.tomcools.atprotocol.codegen.GenerationContext;
import be.tomcools.atprotocol.codegen.lexicon.LexRef;
import be.tomcools.atprotocol.codegen.lexicon.LexType;
import be.tomcools.atprotocol.codegen.lexicon.NSID;
import be.tomcools.atprotocol.codegen.lexicon.primitives.LexBoolean;
import be.tomcools.atprotocol.codegen.lexicon.primitives.LexInteger;
import be.tomcools.atprotocol.codegen.lexicon.primitives.LexString;
import com.squareup.javapoet.*;
import java.io.IOException;
import javax.annotation.processing.Generated;
import javax.lang.model.element.Modifier;
import org.apache.commons.lang3.StringUtils;

public class CodeGen {

	GenerationContext context;

	public CodeGen(GenerationContext context) {
		this.context = context;
	}

	public void generateClasses() {
		context.files().forEach(c -> {
			c.getObjects().forEach(this::generateClassForObject);
		});
	}

	private void generateClassForObject(ObjectInDocument doc) {
		TypeSpec.Builder classfile = TypeSpec.classBuilder(doc.name()).addModifiers(Modifier.PUBLIC)
				.addAnnotation(AnnotationSpec.builder(Generated.class)
						.addMember("value", "$S", "be.tomcools.lexicongen.CodeGen").build());

		doc.obj().properties().forEach((k, v) -> {
			addProperty(classfile, doc, k, v);
		});

		JavaFile javaFile = JavaFile.builder(determinePackageName(doc), classfile.build()).build();

		try {
			javaFile.writeTo(System.out);
			javaFile.writeTo(context.outputDirectory());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static String determinePackageName(ObjectInDocument doc) {
		NSID identifier = doc.doc().id();
		if (identifier.getName().equalsIgnoreCase(doc.name())) {
			return identifier.getDomain();
		} else {
			return identifier.toString();
		}
	}

	private static void addProperty(TypeSpec.Builder classfile, ObjectInDocument doc, String propertyName, LexType v) {
		TypeName type = determineType(doc, v);

		FieldSpec field = FieldSpec.builder(type, propertyName).addModifiers(Modifier.PRIVATE)
				.addJavadoc(v.description() + System.lineSeparator()).build();

		MethodSpec getter = MethodSpec.methodBuilder("get" + StringUtils.capitalize(propertyName)).returns(type)
				.addStatement("return " + propertyName).build();

		classfile.addField(field);
		classfile.addMethod(getter);
	}

	private static TypeName determineType(ObjectInDocument doc, LexType v) {
		if (v.getClass() == LexString.class) {
			return TypeName.get(String.class);
		} else if (v.getClass() == LexBoolean.class) {
			return TypeName.get(Boolean.class);
		} else if (v.getClass() == LexInteger.class) {
			return TypeName.get(Integer.class);
		} else if (v.getClass() == LexRef.class) {
			return determineReferenceClassName((LexRef) v, doc);
		} else {
			return TypeName.get(String.class);
		}
	}

	private static ClassName determineReferenceClassName(LexRef v, ObjectInDocument doc) {
		if (v.getRef().startsWith("#")) {
			// internal reference
			String className = v.getRef().replace("#", "");
			return ClassName.get(doc.doc().id().toString(), StringUtils.capitalize(className));
		} else if (v.getRef().contains("#")) {
			// references other
			String[] expectedReference = v.getRef().split("#");
			return ClassName.get(expectedReference[0], StringUtils.capitalize(expectedReference[1]));
		} else {
			// edge case, where the entire other document references is an Object
			String[] parts = v.getRef().split("\\.");
			String name = parts[parts.length - 1];
			String packageRef = v.getRef().replace("." + name, "");
			return ClassName.get(packageRef, StringUtils.capitalize(name));
		}
	}
}
