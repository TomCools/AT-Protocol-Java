package be.tomcools.atprotocol.codegen.lexicon.primitives;

import be.tomcools.atprotocol.codegen.lexicon.LexType;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LexInteger implements LexType {
	private Map<String, Object> dynamicValues;

	private String description;
	private Integer defaultValue;
	private Integer minimum;
	private Integer maximum;
	private List<Integer> enumeration;
	private Integer constant;

	public LexInteger(String description, Integer defaultValue, Integer minimum, Integer maximum,
			List<Integer> enumeration, Integer constant) {
		this.description = description;
		this.defaultValue = defaultValue;
		this.minimum = minimum;
		this.maximum = maximum;
		this.enumeration = enumeration;
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

	public LexInteger() {
		this.dynamicValues = new LinkedHashMap<>();
	}

	public Integer getDefaultValue() {
		return defaultValue;
	}

	public Integer getMinimum() {
		return minimum;
	}

	public Integer getMaximum() {
		return maximum;
	}

	public List<Integer> getEnumeration() {
		return enumeration;
	}

	public Integer getConstant() {
		return constant;
	}

	@Override
	public String description() {
		return description;
	}
}
