package be.tomcools.atprotocol.codegen.lexicon;

import be.tomcools.atprotocol.codegen.lexicon.types.LexXrpcBody;
import be.tomcools.atprotocol.codegen.lexicon.types.LexXrpcError;
import java.util.List;
import java.util.Map;

public record LexXrpcProcedure(String description, Map<String, LexType> parameters, LexXrpcBody input,
		LexXrpcBody output, List<LexXrpcError> errors) implements LexType {
}
