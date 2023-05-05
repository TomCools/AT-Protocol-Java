package be.tomcools.atprotocol.codegen.generator;

import be.tomcools.atprotocol.codegen.generator.objects.ObjectInDocument;
import be.tomcools.atprotocol.codegen.generator.procedure.ProcedureInDocument;
import be.tomcools.atprotocol.codegen.lexicon.LexObject;
import be.tomcools.atprotocol.codegen.lexicon.LexXrpcProcedure;
import be.tomcools.atprotocol.codegen.lexicon.LexiconDoc;
import be.tomcools.atprotocol.codegen.tools.PropertyRetriever;
import java.util.List;
import java.util.Map;

import static be.tomcools.atprotocol.codegen.tools.PropertyRetriever.getObjectClass;
import static be.tomcools.atprotocol.codegen.tools.PropertyRetriever.getProcedure;

public record GenerationContextFile(LexiconDoc doc, Map<String, Object> flatJson) {

	public List<ObjectInDocument> getObjects() {
		return flatJson.entrySet().stream().filter(e -> e.getKey().endsWith("type")
						&& e.getValue().equals("object"))
				.map(e -> {
					String objectKey = e.getKey().replace(".type", ""); // type is cut, so we get to the higher level
					return new ObjectInDocument(doc, objectKey, getObjectClass(doc, objectKey));
				}).toList();
	}

	public List<ProcedureInDocument> getProcedures() {
		return flatJson.entrySet().stream()
				.filter(e -> e.getKey().endsWith("type") && e.getValue().equals("procedure"))
				.map(e -> {
					String objectKey = e.getKey().replace(".type", ""); // type is cut, so we get to the higher level
					return new ProcedureInDocument(doc,  getProcedure(doc, objectKey));
				})
				.toList();
	}
}
