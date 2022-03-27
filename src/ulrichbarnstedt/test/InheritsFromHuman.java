package ulrichbarnstedt.test;

import ulrichbarnstedt.lib.json.AnnotationJSONSerializer;
import ulrichbarnstedt.lib.json.JSONObject;
import ulrichbarnstedt.lib.json.JSONProperty;

@JSONObject
@JSONProperty
public class InheritsFromHuman extends Human {
    @JSONProperty private String myNewProperty;

    public InheritsFromHuman (String firstName, String lastName, long socSecNum, Human someOtherGuy, String myNewProperty) {
        super(firstName, lastName, socSecNum, someOtherGuy);
        this.myNewProperty = myNewProperty;
    }

    public static void main (String[] args) {
        InheritsFromHuman test = new InheritsFromHuman("a", "b", 123, null, "eeeeeeeee");
        System.out.println(AnnotationJSONSerializer.toJSON(test));
    }
}
