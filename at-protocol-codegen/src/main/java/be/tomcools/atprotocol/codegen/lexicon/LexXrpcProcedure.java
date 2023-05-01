package be.tomcools.atprotocol.codegen.lexicon;

import be.tomcools.atprotocol.codegen.lexicon.types.LexXrpcBody;
import be.tomcools.atprotocol.codegen.lexicon.types.LexXrpcError;
import java.util.List;

public record LexXrpcProcedure(String description, LexXrpcBody input, LexXrpcBody output,
		List<LexXrpcError> errors) implements LexType {
}
