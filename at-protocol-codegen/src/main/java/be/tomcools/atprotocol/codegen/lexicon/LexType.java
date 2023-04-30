package be.tomcools.atprotocol.codegen.lexicon;

import be.tomcools.atprotocol.codegen.lexicon.primitives.LexBoolean;
import be.tomcools.atprotocol.codegen.lexicon.primitives.LexInteger;
import be.tomcools.atprotocol.codegen.lexicon.primitives.LexNumber;
import be.tomcools.atprotocol.codegen.lexicon.primitives.LexString;
import be.tomcools.atprotocol.codegen.lexicon.types.*;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({@JsonSubTypes.Type(value = LexBlob.class, name = "blob"),
		@JsonSubTypes.Type(value = LexAudio.class, name = "audio"),
		@JsonSubTypes.Type(value = LexVideo.class, name = "video"),
		@JsonSubTypes.Type(value = LexXrpcQuery.class, name = "query"),
		@JsonSubTypes.Type(value = LexXrpcProcedure.class, name = "procedure"),
		@JsonSubTypes.Type(value = LexRecord.class, name = "record"),
		@JsonSubTypes.Type(value = LexObject.class, name = "object"),
		@JsonSubTypes.Type(value = LexImage.class, name = "image"),
		@JsonSubTypes.Type(value = LexToken.class, name = "token"),
		@JsonSubTypes.Type(value = LexRef.class, name = "ref"),
		@JsonSubTypes.Type(value = LexBlob.class, name = "blob"),
		@JsonSubTypes.Type(value = LexArray.class, name = "array"),
		@JsonSubTypes.Type(value = LexString.class, name = "string"),
		@JsonSubTypes.Type(value = LexUnion.class, name = "union"),
		@JsonSubTypes.Type(value = LexInteger.class, name = "integer"),
		@JsonSubTypes.Type(value = LexUnknown.class, name = "unknown"),
		@JsonSubTypes.Type(value = LexNumber.class, name = "number"),
		@JsonSubTypes.Type(value = LexBoolean.class, name = "boolean")})
public interface LexType {
	String description();
}
