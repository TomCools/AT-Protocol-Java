package be.tomcools.atprotocol.codegen.lexicon;

import be.tomcools.atprotocol.codegen.lexicon.types.LexQueryParameters;
import be.tomcools.atprotocol.codegen.lexicon.types.LexXrpcBody;
import be.tomcools.atprotocol.codegen.lexicon.types.LexXrpcError;
import java.util.List;

public record LexXrpcQuery(String description, LexQueryParameters parameters, LexXrpcBody output,
		List<LexXrpcError> errors) implements LexType {
}
