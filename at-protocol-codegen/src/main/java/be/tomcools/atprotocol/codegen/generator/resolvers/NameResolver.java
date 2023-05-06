package be.tomcools.atprotocol.codegen.generator.resolvers;

import be.tomcools.atprotocol.codegen.generator.objects.ObjectInDocument;
import be.tomcools.atprotocol.codegen.generator.procedure.ProcedureInDocument;
import be.tomcools.atprotocol.codegen.generator.query.QueryInDocument;
import be.tomcools.atprotocol.codegen.lexicon.NSID;
import be.tomcools.atprotocol.codegen.lexicon.Schema;
import be.tomcools.atprotocol.codegen.lexicon.SchemaRef;
import com.squareup.javapoet.ClassName;
import org.apache.commons.lang3.StringUtils;

public class NameResolver {
	public static final String FRAMEWORK_PACKAGE_NAME = "be.tomcools.atprotocol.client";
	public static final String CLIENT_CLASS_NAME = "AtpApi";
	public static final String JSON_HANDLER_CLASS_NAME = "AtpJsonHandler";
	public static final String CLIENT_EXCEPTION_CLASS_NAME = "AtpApiException";

	public ClassNameDefinition resolve(ObjectInDocument obj) {
		return new ClassNameDefinition(determinePackageName(obj), determineClassName(obj));
	}

	public String resolveMethodName(ProcedureInDocument procedure) {
		return procedure.doc().id().getName();
	}

	public String resolveMethodName(QueryInDocument query) {
		return query.doc().id().getName();
	}

	private String determinePackageName(ObjectInDocument obj) {
		if (obj.objectKey().endsWith("defs.main")) {
			return obj.doc().id().getDomain().toLowerCase();
		} else {
			return obj.doc().id().toString().toLowerCase();
		}
	}

	/**
	 * @return the expected class objectKey;
	 */
	public String determineClassName(ObjectInDocument obj) {
		String objectKey = obj.objectKey();
		// schema should not be taken into account for the objectKey, so remove it.
		String[] split = objectKey.replace(".schema", "").split("\\.");
		if (objectKey.contains(".schema")) {
			// return special object name with schema
			return StringUtils.capitalize(obj.doc().id().getName()) + StringUtils.capitalize(split[split.length - 1]);
		} else if (objectKey.endsWith("defs.main")) { // single object in file, use document name.
			return StringUtils.capitalize(obj.doc().id().getName());
		} else if (objectKey.endsWith(".record")) { // Record Definition, we want to postfix those with Record
			return StringUtils.capitalize(obj.doc().id().getName()) + "Record";
		} else {
			// return regular object name
			return StringUtils.capitalize(split[split.length - 1]);
		}
	}

	public ClassName determineClassNameForSchema(NSID documentId, Schema schema, String postFix) {
		String docFullName = documentId.toString().toLowerCase();
		if (schema instanceof SchemaRef) {
			return ClassName.get(docFullName,
					StringUtils.capitalize(((SchemaRef) schema).getReference().replace("#", "")));
		} else {
			return ClassName.get(docFullName, StringUtils.capitalize(documentId.getName() + postFix));
		}
	}

}
