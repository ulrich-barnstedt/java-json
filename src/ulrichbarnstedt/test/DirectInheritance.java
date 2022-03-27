package ulrichbarnstedt.test;

import ulrichbarnstedt.lib.json.AnnotationJSONSerializer;
import ulrichbarnstedt.lib.json.JSONObject;
import ulrichbarnstedt.lib.json.JSONProperty;

@JSONObject
public class DirectInheritance extends AnnotationJSONSerializer {
    @JSONProperty private static String test = "Static";
    @JSONProperty private String test2 = "non static";

    public static void main (String[] args) {
        System.out.println(new DirectInheritance().toJSON());
    }
}
