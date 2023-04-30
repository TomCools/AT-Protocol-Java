package be.tomcools.atprotocol.codegen.lexicon;

public record LexUnknown() implements LexType {
	@Override
	public String description() {
		return "Unknown";
	}
}
