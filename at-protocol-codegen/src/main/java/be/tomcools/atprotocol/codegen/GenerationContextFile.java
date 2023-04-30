package be.tomcools.atprotocol.codegen;

import be.tomcools.atprotocol.codegen.generator.ObjectInDocument;
import be.tomcools.atprotocol.codegen.lexicon.LexObject;
import be.tomcools.atprotocol.codegen.lexicon.LexiconDoc;
import be.tomcools.atprotocol.codegen.tools.PropertyRetriever;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

public record GenerationContextFile(LexiconDoc doc, Map<String, Object> flatJson) {

	public List<ObjectInDocument> getObjects() {
		return flatJson.entrySet().stream().filter(e -> e.getKey().endsWith("type") && e.getValue().equals("object"))
				.map(e -> {
					String objectKey = e.getKey().replace(".type", "");
					if (objectKey.equals("defs.main")) {
						// special case where the whole file is about 1 object, use Document ID as class
						return new ObjectInDocument(doc, determineName(doc.id().toString()), getObjectClass(objectKey));
					} else {
						return new ObjectInDocument(doc, determineName(objectKey), getObjectClass(objectKey));
					}
				}).toList();
	}

	private String determineName(String objectKey) {
		String[] split = objectKey.split("\\.");
		return StringUtils.capitalize(split[split.length - 1]);
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
