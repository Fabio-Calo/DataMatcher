package matcher;

import java.lang.annotation.*;
import java.util.HashMap;
import java.util.Map;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MatchWith {
  boolean primary() default false;
  String field() default "";
}
