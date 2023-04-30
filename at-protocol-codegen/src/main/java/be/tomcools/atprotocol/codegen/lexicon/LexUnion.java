package be.tomcools.atprotocol.codegen.lexicon;

import java.util.List;

public record LexUnion(List<SchemaRef> refs) implements LexType {
	@Override
	public String description() {
		return "";
	}
}
