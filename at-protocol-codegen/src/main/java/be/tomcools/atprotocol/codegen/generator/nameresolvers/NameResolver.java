package be.tomcools.atprotocol.codegen.generator.nameresolvers;

import be.tomcools.atprotocol.codegen.generator.objects.ObjectInDocument;
import be.tomcools.atprotocol.codegen.generator.procedure.ProcedureInDocument;
import org.apache.commons.lang3.StringUtils;

public class NameResolver {
    public ClassNameDefinition resolve(ObjectInDocument obj) {
        return new ClassNameDefinition(determinePackageName(obj), determineClassName(obj));
    }

    public String resolveMethodName(ProcedureInDocument procedure) {
        return procedure.doc().id().getName();
    }

    private String determinePackageName(ObjectInDocument obj) {
        if(obj.objectKey().endsWith("defs.main")) {
            return obj.doc().id().getDomain().toLowerCase();
        } else {
            return obj.doc().id().toString().toLowerCase();
        }
    }

    /**
     * @return the expected class objectKey;
     */
    public String determineClassName(ObjectInDocument obj) {
        String objectKey = obj.objectKey();
        // schema should not be taken into account for the objectKey, so remove it.
        String[] split = objectKey.replace(".schema", "").split("\\.");
        if (objectKey.contains(".schema")) {
            // return special object name with schema
            return StringUtils.capitalize(obj.doc().id().getName()) + StringUtils.capitalize(split[split.length - 1]);
        } else {
            // return regular object name
            return StringUtils.capitalize(split[split.length - 1]);
        }
    }
}
