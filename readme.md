## java-json
`ulrichbarnstedt.lib.json`

A small library to make converting classes to JSON easier.  
Supports inheritance (will crawl all superclasses).  
Note: Classes themselves can also be annotated for use cases such as deserializing the data later on.  

### Extending functionality

Classes that cannot be converted using the provided annotations but should still be able to support the toJSON method
(which is handy because this allows objects as properties to work) can be created using the `JSONSerializable` interface.  
Example:
```java
package ulrichbarnstedt.test;

import ulrichbarnstedt.lib.json.JSONSerializable;

public class ClassWithCustomSerializer implements JSONSerializable {

    @Override
    public String toJSON (int indent) {
        // return the JSON of this class at the correct indent
    }
}
```

### Example

```java
package ulrichbarnstedt.test;

import ulrichbarnstedt.lib.json.AnnotationJSONSerializer;
import ulrichbarnstedt.lib.json.JSONProperty;

@JSONProperty
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
  "class" : "Human",
  "firstName" : "jef",
  "lAsTnAmE" : "jef",
  "someOtherGuy" : {
    "class" : "Human",
    "firstName" : "sub",
    "lAsTnAmE" : "guy",
    "someOtherGuy" : null,
    "socSecNum" : 54321
  },
  "socSecNum" : 12345
}
```