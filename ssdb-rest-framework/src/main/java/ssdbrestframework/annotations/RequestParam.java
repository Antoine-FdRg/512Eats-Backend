package ssdbrestframework.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation for defining a request parameter, must be used in conjunction with {@link Endpoint}, just before the parameter that is the request parameter
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestParam {
    /**
     * The name of the request parameter
     * @return the name of the request parameter
     */
    String value();
}