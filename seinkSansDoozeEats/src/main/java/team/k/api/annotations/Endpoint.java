package team.k.api.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation for defining an endpoint
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Endpoint {
    /**
     * The path of the endpoint
     * @return the path of the endpoint
     */
    String path();

    /**
     * The HTTP method of the endpoint
     * @return the HTTP method of the endpoint
     */
    String method() default "GET";
}
