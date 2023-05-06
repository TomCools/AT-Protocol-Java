package be.tomcools.atprotocol.codegen.tools;

import be.tomcools.atprotocol.codegen.lexicon.LexObject;
import be.tomcools.atprotocol.codegen.lexicon.LexXrpcProcedure;
import be.tomcools.atprotocol.codegen.lexicon.LexXrpcQuery;
import be.tomcools.atprotocol.codegen.lexicon.LexiconDoc;
import java.lang.reflect.Method;
import java.util.Map;

public class PropertyRetriever {

	public static LexXrpcQuery getQuery(LexiconDoc doc, String propertyPath) {
		Object property = PropertyRetriever.getNestedProperty(doc, propertyPath);
		if (property instanceof LexXrpcQuery) {
			return (LexXrpcQuery) property;
		} else {
			throw new RuntimeException("Object not LexObject");
		}
	}

	public static LexXrpcProcedure getProcedure(LexiconDoc doc, String propertyPath) {
		Object property = PropertyRetriever.getNestedProperty(doc, propertyPath);
		if (property instanceof LexXrpcProcedure) {
			return (LexXrpcProcedure) property;
		} else {
			throw new RuntimeException("Object not LexObject");
		}
	}

	public static LexObject getObjectClass(LexiconDoc doc, String propertyPath) {
		Object property = PropertyRetriever.getNestedProperty(doc, propertyPath);
		if (property instanceof LexObject) {
			return (LexObject) property;
		} else {
			throw new RuntimeException("Object not LexObject");
		}
	}

	private static Object getNestedProperty(Object o, String nestedProperty) {
		String[] split = nestedProperty.split("\\.");
		Object result = o;
		for (String s : split) {
			if (Map.class.isAssignableFrom(result.getClass())) {
				result = ((Map) result).get(s);
			} else {
				result = getUsingReflection(result, s);
			}
		}
		return result;
	}

	private static Object getUsingReflection(Object result, String s) {
		Method declaredField = null;
		try {
			declaredField = result.getClass().getDeclaredMethod(s);
			result = declaredField.invoke(result);
			return result;
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
	}
}
