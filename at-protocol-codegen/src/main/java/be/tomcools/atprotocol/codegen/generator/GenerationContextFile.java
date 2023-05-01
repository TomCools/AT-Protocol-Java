package be.tomcools.atprotocol.codegen.generator;

import be.tomcools.atprotocol.codegen.generator.objects.ObjectInDocument;
import be.tomcools.atprotocol.codegen.generator.procedure.ProcedureInDocument;
import be.tomcools.atprotocol.codegen.lexicon.LexObject;
import be.tomcools.atprotocol.codegen.lexicon.LexXrpcProcedure;
import be.tomcools.atprotocol.codegen.lexicon.LexiconDoc;
import be.tomcools.atprotocol.codegen.tools.PropertyRetriever;
import java.util.List;
import java.util.Map;

public record GenerationContextFile(LexiconDoc doc, Map<String, Object> flatJson) {

	public List<ObjectInDocument> getObjects() {
		return flatJson.entrySet().stream().filter(e -> e.getKey().endsWith("type") && e.getValue().equals("object")) // get
																														// objects
				.map(e -> {
					String objectKey = e.getKey().replace(".type", ""); // type is cut, so we get to the higher level
					if (objectKey.equals("defs.main")) {
						// special case where the whole file is about 1 object, use Document ID as class
						return new ObjectInDocument(doc, doc.id().toString(), getObjectClass(objectKey));
					} else {
						return new ObjectInDocument(doc, objectKey, getObjectClass(objectKey));
					}
				}).toList();
	}

	public List<ProcedureInDocument> getProcedures() {
		return flatJson.entrySet().stream().filter(e -> e.getKey().endsWith("type") && e.getValue().equals("procedure"))
				.map(e -> {
					String objectKey = e.getKey().replace(".type", ""); // type is cut, so we get to the higher level
					// entry to get the objectKey
					if (objectKey.equals("defs.main")) {
						// special case where the whole file is about 1 object, use Document ID as class
						return new ProcedureInDocument(doc, doc.id().toString(), getProcedure(objectKey));
					} else {
						return new ProcedureInDocument(doc, objectKey, getProcedure(objectKey));
					}
				}).toList();
	}

	private LexXrpcProcedure getProcedure(String nestedProperty) {
		Object property = PropertyRetriever.getNestedProperty(doc, nestedProperty);
		if (property instanceof LexXrpcProcedure) {
			return (LexXrpcProcedure) property;
		} else {
			throw new RuntimeException("Object not LexObject");
		}
	}

	private LexObject getObjectClass(String nestedProperty) {
		Object property = PropertyRetriever.getNestedProperty(doc, nestedProperty);
		if (property instanceof LexObject) {
			return (LexObject) property;
		} else {
			throw new RuntimeException("Object not LexObject");
		}
	}
}
