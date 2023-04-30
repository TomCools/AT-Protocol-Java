package be.tomcools.atprotocol.codegen.generator;

import be.tomcools.atprotocol.codegen.lexicon.LexRef;
import be.tomcools.atprotocol.codegen.lexicon.LexType;
import be.tomcools.atprotocol.codegen.lexicon.NSID;
import be.tomcools.atprotocol.codegen.lexicon.primitives.LexBoolean;
import be.tomcools.atprotocol.codegen.lexicon.primitives.LexInteger;
import be.tomcools.atprotocol.codegen.lexicon.primitives.LexString;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import org.apache.commons.lang3.StringUtils;

public class TypeResolver {

    public static TypeName determineType(NSID documentId, LexType v) {
        if (v.getClass() == LexString.class) {
            return TypeName.get(String.class);
        } else if (v.getClass() == LexBoolean.class) {
            return TypeName.get(Boolean.class);
        } else if (v.getClass() == LexInteger.class) {
            return TypeName.get(Integer.class);
        } else if (v.getClass() == LexRef.class) {
            return determineReferenceClassName((LexRef) v, documentId);
        } else {
            return TypeName.get(String.class);
        }
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
