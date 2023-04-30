package be.tomcools.atprotocol.codegen.lexicon;

public final class LexRef implements LexType {
	private String ref;

	public LexRef(String ref) {
		this.ref = ref;
	}

	public LexRef() {
	}

	public String getRef() {
		return ref;
	}

	@Override
	public String description() {
		return "null";
	}
}
