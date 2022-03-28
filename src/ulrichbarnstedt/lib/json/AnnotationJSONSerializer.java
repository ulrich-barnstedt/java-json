package ulrichbarnstedt.lib.json;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.LinkedHashMap;

abstract public class AnnotationJSONSerializer {
    private static String processFieldValue (Object instance, int indent, Field field) {
        Object fieldValue;
        try {
            //make accessible to ensure the annotation works
            field.setAccessible(true);

            fieldValue = field.get(instance);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return "null";
        }

        if (fieldValue == null) {
            return "null";
        }

        if (fieldValue instanceof String) {
            return "\"" + fieldValue + "\"";
        }

        if (fieldValue instanceof JSONSerializable) {
            return ((JSONSerializable) fieldValue).toJSON(indent + 1);
        }

        if (fieldValue.getClass().isAnnotationPresent(JSONObject.class)) {
            return AnnotationJSONSerializer.toJSON(fieldValue, indent + 1);
        }

        return "\"" + fieldValue + "\"";
    }

    private static LinkedHashMap<String, String> parseAnnotations (Object instance, int indent) {
        LinkedHashMap<String, String> content = new LinkedHashMap<>();

        //check if the class itself is annotated, if yes take the name as a string
        if (instance.getClass().isAnnotationPresent(JSONProperty.class)) {
            JSONProperty annotation = instance.getClass().getAnnotation(JSONProperty.class);
            String key = annotation.key().equals("") ? "class" : annotation.key();
            String value = "\"" + instance.getClass().getSimpleName() + "\"";

            content.put(key, value);
        }

        //crawl through the whole class tree to get inherited fields
        Class<?> hierachy = instance.getClass();
        while (hierachy != Object.class) {
            for (Field field : hierachy.getDeclaredFields()) {
                //only fields with the JSONProperty annotation
                if (!field.isAnnotationPresent(JSONProperty.class)) continue;

                JSONProperty annotation = field.getAnnotation(JSONProperty.class);
                String key = annotation.key().equals("") ? field.getName() : annotation.key();

                content.put(key, AnnotationJSONSerializer.processFieldValue(instance, indent, field));
            }

            if (hierachy.getSuperclass().isAnnotationPresent(JSONObject.class) || JSONSerializable.class.isAssignableFrom(hierachy))
                hierachy = hierachy.getSuperclass();
            else break;
        }

        return content;
    }

    private static String buildIndent (int indent) {
        StringBuilder indentBuilder = new StringBuilder();
        for (int i = 0; i < indent; i++) indentBuilder.append("  ");
        return indentBuilder.toString();
    }

    private static String toJSON (Object instance, int indent) {
        if (instance instanceof JSONSerializable) return ((JSONSerializable) instance).toJSON(indent + 1);
        if (!instance.getClass().isAnnotationPresent(JSONObject.class)) return "\"" + instance.toString() + "\"";

        LinkedHashMap<String, String> content = AnnotationJSONSerializer.parseAnnotations(instance, indent);
        String indentString = AnnotationJSONSerializer.buildIndent(indent);

        StringBuilder json = new StringBuilder();
        json.append("{\n");

        Iterator<String> itr = content.keySet().iterator();
        while (itr.hasNext()) {
            String key = itr.next();
            json.append(indentString)
                .append("  \"")
                .append(key)
                .append("\" : ")
                .append(content.get(key))
                .append(itr.hasNext() ? "," : "")
                .append("\n");
        }

        json.append(indentString).append("}");
        return json.toString();
    }

    public static String toJSON (Object instance) {
        return AnnotationJSONSerializer.toJSON(instance, 0);
    }


    //for inheriting classes

    public String toJSON () {
        return this.toJSON(0);
    }

    public String toJSON (int indent) {
        return AnnotationJSONSerializer.toJSON(this, indent);
    }
}
