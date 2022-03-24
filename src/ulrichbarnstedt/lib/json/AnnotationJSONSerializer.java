package ulrichbarnstedt.lib.json;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.LinkedHashMap;

abstract public class AnnotationJSONSerializer implements JSONSerializable {
    private String processFieldValue (int indent, Field field) {
        Object fieldValue;
        try {
            //make accessible to ensure the annotation works
            field.setAccessible(true);

            fieldValue = field.get(this);
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

        return fieldValue.toString();
    }

    private LinkedHashMap<String, String> parseAnnotations (int indent) {
        LinkedHashMap<String, String> content = new LinkedHashMap<>();

        //check if the class itself is annotated, if yes take the name as a string
        if (this.getClass().isAnnotationPresent(JSONProperty.class)) {
            JSONProperty annotation = this.getClass().getAnnotation(JSONProperty.class);
            String key = annotation.key().equals("") ? "class" : annotation.key();
            String value = "\"" + this.getClass().getSimpleName() + "\"";

            content.put(key, value);
        }

        //crawl through the whole class tree to get inherited fields
        Class hierachy = this.getClass();
        while (hierachy != Object.class) {
            for (Field field : hierachy.getDeclaredFields()) {
                //only fields with the JSONProperty annotation
                if (!field.isAnnotationPresent(JSONProperty.class)) continue;

                JSONProperty annotation = field.getAnnotation(JSONProperty.class);
                String key = annotation.key().equals("") ? field.getName() : annotation.key();

                content.put(key, processFieldValue(indent, field));
            }

            hierachy = hierachy.getSuperclass();
        }

        return content;
    }

    private String buildIndent (int indent) {
        StringBuilder indentBuilder = new StringBuilder();
        for (int i = 0; i < indent; i++) indentBuilder.append("  ");
        return indentBuilder.toString();
    }

    @Override
    public String toJSON (int indent) {
        LinkedHashMap<String, String> content = this.parseAnnotations(indent);
        String indentString = this.buildIndent(indent);

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
}
