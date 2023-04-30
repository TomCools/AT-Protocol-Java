package be.tomcools.atprotocol.codegen.lexicon;

public class LexArray implements LexType {
	private String description;
	private Object items;
	private Integer minLength;
	private Integer maxLength;

	public LexArray(String description, Object items, Integer minLength, Integer maxLength) {
		this.description = description;
		this.items = items;
		this.minLength = minLength;
		this.maxLength = maxLength;
	}

	public LexArray() {
	}

	public String getDescription() {
		return description;
	}

	public Object getItems() {
		return items;
	}

	public Integer getMinLength() {
		return minLength;
	}

	public Integer getMaxLength() {
		return maxLength;
	}

	@Override
	public String description() {
		return this.description;
	}
}
