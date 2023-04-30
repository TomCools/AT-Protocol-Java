package be.tomcools.atprotocol.codegen.lexicon.primitives;

import be.tomcools.atprotocol.codegen.lexicon.LexType;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.util.LinkedHashMap;
import java.util.Map;

public final class LexBoolean implements LexType {
	private Map<String, Object> dynamicValues;
	private String description;
	private Boolean defaultValue;
	private Boolean constant;

	public LexBoolean() {
		this.dynamicValues = new LinkedHashMap<>();
	}

	public LexBoolean(String description, Boolean defaultValue, Boolean constant) {
		this.description = description;
		this.defaultValue = defaultValue;
		this.constant = constant;
		this.dynamicValues = new LinkedHashMap<>();
	}

	@JsonAnySetter
	public void ignored(String name, Object value) {
		this.dynamicValues.put(name, value);
	}

	@JsonAnyGetter
	public Map<String, Object> getProperties() {
		return dynamicValues;
	}

	@Override
	public String description() {
		return description;
	}

	public Boolean defaultValue() {
		return defaultValue;
	}

	public Boolean constant() {
		return constant;
	}
}
