package ssdbrestframework.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation for defining a path variable, must be used in conjunction with {@link Endpoint}, just before the parameter that is the path variable
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface PathVariable {
    /**
     * The name of the path variable
     *
     * @return the name of the path variable
     */
    String value();
}
