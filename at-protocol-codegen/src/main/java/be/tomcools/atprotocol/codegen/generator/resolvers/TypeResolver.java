package be.tomcools.atprotocol.codegen.generator.resolvers;

import be.tomcools.atprotocol.codegen.lexicon.LexRef;
import be.tomcools.atprotocol.codegen.lexicon.LexType;
import be.tomcools.atprotocol.codegen.lexicon.NSID;
import be.tomcools.atprotocol.codegen.lexicon.primitives.LexBoolean;
import be.tomcools.atprotocol.codegen.lexicon.primitives.LexInteger;
import be.tomcools.atprotocol.codegen.lexicon.primitives.LexString;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TypeResolver {

	public static TypeName determineType(NSID documentId, LexType v) {
		if (v.getClass() == LexString.class) {
			return getStringSubtype((LexString) v);
		} else if (v.getClass() == LexBoolean.class) {
			return TypeName.get(Boolean.class);
		} else if (v.getClass() == LexInteger.class) {
			return TypeName.get(Integer.class);
		} else if (v.getClass() == LexRef.class) {
			return determineReferenceClassName((LexRef) v, documentId);
		} else {
			return TypeName.get(Object.class);
		}
	}

	private static TypeName getStringSubtype(LexString v) {
		String format = v.getFormat();
		if(format == null) {
			return TypeName.get(String.class);
		}
		if(format.equals("datetime")) {
			return TypeName.get(LocalDateTime.class);
		}
		if(format.equals("date")) {
			return TypeName.get(LocalDate.class);
		}
		// TODO DID and others;
		return TypeName.get(String.class);
	}

	public static ClassName determineReferenceClassName(LexRef v, NSID doc) {
		if (v.getRef().startsWith("#")) {
			// internal reference
			String className = v.getRef().replace("#", "");
			return ClassName.get(doc.toString(), StringUtils.capitalize(className));
		} else if (v.getRef().contains("#")) {
			// references other
			String[] expectedReference = v.getRef().split("#");
			return ClassName.get(expectedReference[0], StringUtils.capitalize(expectedReference[1]));
		} else {
			// edge case, where the entire other document references is an Object
			String[] parts = v.getRef().split("\\.");
			String name = parts[parts.length - 1];
			String packageRef = v.getRef().replace("." + name, "");
			return ClassName.get(packageRef, StringUtils.capitalize(name));
		}
	}
}
