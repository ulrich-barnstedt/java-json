package ulrichbarnstedt.test;

import ulrichbarnstedt.lib.json.AnnotationJSONSerializer;
import ulrichbarnstedt.lib.json.JSONSerializable;

public class Custom implements JSONSerializable {
    @Override
    public String toJSON (int indent) {
        return "test content";
    }

    public static void main (String[] args) {
        Custom x = new Custom();
        System.out.println(AnnotationJSONSerializer.toJSON(x));
    }
}
