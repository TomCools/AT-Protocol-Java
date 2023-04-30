package be.tomcools.atprotocol.codegen.generator.procedure;

import be.tomcools.atprotocol.codegen.ATProtocolCodeGenerator;
import be.tomcools.atprotocol.codegen.errors.ATPCodeGenException;
import be.tomcools.atprotocol.codegen.lexicon.Schema;
import be.tomcools.atprotocol.codegen.lexicon.SchemaRef;
import com.squareup.javapoet.*;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.processing.Generated;
import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class ProcedureClassGenerator {

	public void generateClient(String fqClassName, List<ProcedureInDocument> procedure, File outputDirectory) {
		TypeSpec.Builder classfile = TypeSpec.classBuilder(getClassName(fqClassName))
				.addModifiers(Modifier.PUBLIC)
				.addAnnotation(AnnotationSpec.builder(Generated.class)
						.addMember("value", "$S", ATProtocolCodeGenerator.class.getName()).build());

		for (ProcedureInDocument procedureInDocument : procedure) {
			this.addProcedure(classfile, procedureInDocument);
		}

		JavaFile javaFile = JavaFile.builder(getPackageName(fqClassName), classfile.build()).build();

		try {
			javaFile.writeTo(System.out);
			javaFile.writeTo(outputDirectory);
		} catch (IOException e) {
			throw new ATPCodeGenException("Couldn't write generated file to directory", e);
		}
	}

	private void addProcedure(TypeSpec.Builder classfile, ProcedureInDocument procedure) {
		MethodSpec method = MethodSpec.methodBuilder(procedure.determineMethodName())
				.addModifiers(Modifier.PUBLIC)
				.addParameter(determineInputClass(procedure),"input")
				.returns(determineOutputClass(procedure))
				.addStatement("return null") //TODO Implement REST CLient :-)
				.build();

		classfile.addMethod(method);
	}

	private static ClassName determineInputClass(ProcedureInDocument procedure) {
		Schema schema = procedure.obj().input().schema();
		String procedureName = procedure.name().toLowerCase();
		if (schema instanceof SchemaRef) {
			return ClassName.get(procedureName, StringUtils.capitalize(((SchemaRef) schema).getReference().replace("#","")));
		} else {
			return ClassName.get(procedureName, "Input");
		}
	}

	private static ClassName determineOutputClass(ProcedureInDocument procedure) {
		Schema schema = procedure.obj().output().schema();
		String procedureName = procedure.name().toLowerCase();
		if (schema instanceof SchemaRef) {
			return ClassName.get(procedureName, StringUtils.capitalize(((SchemaRef) schema).getReference().replace("#","")));
		} else {
			return ClassName.get(procedureName, "Output");
		}
	}
	private String getPackageName(String fqClassName) {
		return fqClassName.replace("." + getClassName(fqClassName), "").toLowerCase();
	}

	private String getClassName(String fqClassName) {
		String[] split = fqClassName.split("\\.");
		return StringUtils.capitalize(split[split.length - 1]);
	}
}
