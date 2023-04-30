package be.tomcools.atprotocol.codegen.tools;

import java.lang.reflect.Method;
import java.util.Map;

public class PropertyRetriever {
	public static Object getNestedProperty(Object o, String nestedProperty) {
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
