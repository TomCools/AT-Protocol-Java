package be.tomcools.atprotocol.codegen.parser;

import be.tomcools.atprotocol.codegen.errors.ATPCodeGenException;
import be.tomcools.atprotocol.codegen.lexicon.LexiconDoc;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LexiconParser {
	public ObjectMapper mapper;

	public LexiconParser() {
		mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
	}

	public LexiconDoc fromJson(String json) {
		try {
			return mapper.readValue(json, LexiconDoc.class);
		} catch (JsonProcessingException e) {
			throw new ATPCodeGenException("Error while reading input file to JSON", e);
		}
	}

	public String toJson(LexiconDoc doc) throws JsonProcessingException {
		return mapper.writeValueAsString(doc);
	}
}
