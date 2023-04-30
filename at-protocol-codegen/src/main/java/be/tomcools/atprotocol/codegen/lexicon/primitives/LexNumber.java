package be.tomcools.atprotocol.codegen.lexicon.primitives;

import be.tomcools.atprotocol.codegen.lexicon.LexType;
import java.util.List;

public class LexNumber implements LexType {
	private String description;
	private Double defaultValue;
	private Double minimum;
	private Double maximum;
	private List<Double> enumeration;
	private Double constant;

	public LexNumber(String description, Double defaultValue, Double minimum, Double maximum, List<Double> enumeration,
			Double constant) {
		this.description = description;
		this.defaultValue = defaultValue;
		this.minimum = minimum;
		this.maximum = maximum;
		this.enumeration = enumeration;
		this.constant = constant;
	}

	public Double getDefaultValue() {
		return defaultValue;
	}

	public Double getMinimum() {
		return minimum;
	}

	public Double getMaximum() {
		return maximum;
	}

	public List<Double> getEnumeration() {
		return enumeration;
	}

	public Double getConstant() {
		return constant;
	}

	@Override
	public String description() {
		return description;
	}
}
