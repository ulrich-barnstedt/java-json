## java-json
`ulrichbarnstedt.lib.json`

A small library to make converting classes to JSON easier.  
Supports inheritance - see explanation below examples.   
Note: Classes themselves can also be annotated for use cases such as deserializing the data later on.  

### Extending functionality

Classes which cannot be converted into JSON using the existing annotations, but should still be integrated into this ecosystem
can be created using the `JSONSerializable` interface.  
Example implementation:

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

### Example code

```java
package ulrichbarnstedt.test;

import ulrichbarnstedt.lib.json.AnnotationJSONSerializer;
import ulrichbarnstedt.lib.json.JSONObject;
import ulrichbarnstedt.lib.json.JSONProperty;

//this annotation integrates this class into the ecosystem, meaning if it is a property of another JSON object
//it will automatically be converted to JSON, else toString of the class is used
//generally speaking, without this annotation the class will not even be attempted to be converted to JSON
@JSONObject
//writing this annotation at class scope will include the class name in the generated JSON
//supports a custom key like all other properties
@JSONProperty(key = "className")
public class Human {
    //a normal property to be included when converting to JSON
    @JSONProperty private String firstName;
    
    //a property to be included, but with a custom key
    @JSONProperty(key = "lAsTnAmE") private String lastName;
    
    //a second Human, as a property
    //will be converted to a JSON object since this class is annotated with @JSONObject
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
        
        //using the standard serializer to convert the class to JSON
        System.out.println(AnnotationJSONSerializer.toJSON(human));
        
        //alternative ways for converting, IF THE CLASS EXTENDS AnnotationJSONSerializer
        //it is not necessary as visible in the way above, but in some cases you might want to use this
        
        //use the method which was inherited, will also work for other classes which inherit from this
        //warning: if this class is not annotated with @JSONObject as explained above, this will result in toString() surrounded with "
        System.out.println(this.toJSON());
        
        //since static methods are inherited this works
        System.out.println(Human.toJSON(this));
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

Inheritance is not explained in the example, but works as expected and will include all inherited properties until it reaches a class which is not annotated with `@JSONObject`.