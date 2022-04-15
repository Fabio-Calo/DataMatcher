package matcher;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MatchWith {
  boolean primary() default false;
  String field() default "";
}
