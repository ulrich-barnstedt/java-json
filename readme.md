## java-json
`ulrichbarnstedt.lib.json`

A small library to make converting classes to JSON easier.

### Example

```java
package ulrichbarnstedt.test;

import ulrichbarnstedt.lib.json.AnnotationJSONSerializer;
import ulrichbarnstedt.lib.json.JSONProperty;

public class Human extends AnnotationJSONSerializer {
    @JSONProperty private String firstName;
    @JSONProperty(key = "lAsTnAmE") private String lastName;
    @JSONProperty private Human someOtherGuy;
    @JSONProperty private long socSecNum;

    public Human (String firstName, String lastName, long socSecNum, Human someOtherGuy) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.socSecNum = socSecNum;
        this.someOtherGuy = someOtherGuy;
    }

    public static void main (String[] args) {
        Human testHuman = new Human("sub", "guy", 54321, null);
        Human human = new Human("jef", "jef", 12345, testHuman);
        System.out.println(human.toJSON());
    }
}
```

Outputs:
```json
{
  "firstName" : "jef",
  "lAsTnAmE" : "jef",
  "someOtherGuy" : {
    "firstName" : "sub",
    "lAsTnAmE" : "guy",
    "someOtherGuy" : null,
    "socSecNum" : 54321
  },
  "socSecNum" : 12345
}
```