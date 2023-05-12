package be.tomcools.atprotocol.codegen.lexicon.primitives;

import be.tomcools.atprotocol.codegen.lexicon.LexType;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LexString implements LexType {

	Map<String, Object> dynamicValues;
	String description;
	String defaultValue;
	Integer minLength;
	Integer maxLength;
	List<String> enumeration;
	String constant;
	List<String> knownValues;
	String format;

	public LexString() {
		this.dynamicValues = new LinkedHashMap<>();
	}

	public LexString(String description, String defaultValue, Integer minLength, Integer maxLength,
			List<String> enumeration, String constant, List<String> knownValues, String format) {
		this.description = description;
		this.defaultValue = defaultValue;
		this.minLength = minLength;
		this.maxLength = maxLength;
		this.enumeration = enumeration;
		this.constant = constant;
		this.knownValues = knownValues;
		this.dynamicValues = new LinkedHashMap<>();
		this.format = format;
	}

	public String description() {
		return description;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public Integer getMinLength() {
		return minLength;
	}

	public Integer getMaxLength() {
		return maxLength;
	}

	public List<String> getEnumeration() {
		return enumeration;
	}

	public String getConstant() {
		return constant;
	}

	public List<String> getKnownValues() {
		return knownValues;
	}

	public String getFormat() {
		return format;
	}

	@JsonAnySetter
	public void ignored(String name, Object value) {
		this.dynamicValues.put(name, value);
	}

	@JsonAnyGetter
	public Map<String, Object> getProperties() {
		return dynamicValues;
	}
}
