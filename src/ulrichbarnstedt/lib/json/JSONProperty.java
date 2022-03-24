package ulrichbarnstedt.lib.json;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
@Inherited
public @interface JSONProperty {
    String key() default "";
}
