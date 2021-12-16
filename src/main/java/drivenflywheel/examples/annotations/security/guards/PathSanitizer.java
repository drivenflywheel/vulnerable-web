package drivenflywheel.examples.annotations.security.guards;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Marker indicating that the annotated method implements path sanitation or validation to ensure that a specified value
 * is safe
 */
@Documented
@Target(ElementType.METHOD)
public @interface PathSanitizer {
    boolean throwsOnUnsafeValues() default false;
}
