package ulrichbarnstedt.test;

import ulrichbarnstedt.lib.json.AnnotationJSONSerializer;
import ulrichbarnstedt.lib.json.JSONObject;
import ulrichbarnstedt.lib.json.JSONProperty;

@JSONObject
public class CustomImpl {
    @JSONProperty private final Custom test = new Custom();

    public static void main (String[] args) {
        CustomImpl x = new CustomImpl();
        System.out.println(AnnotationJSONSerializer.toJSON(x));
    }
}
