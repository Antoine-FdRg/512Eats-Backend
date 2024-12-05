package ssdbrestframework.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation for defining an example response for an endpoint
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiResponseExample {
    Class<?> value();

    boolean isArray() default false;
}
