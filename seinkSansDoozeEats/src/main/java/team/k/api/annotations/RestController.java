package team.k.api.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation for defining a REST controller, use it on a class that contains endpoints
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface RestController {
    /**
     * The base path of the controller
     * @return the base path of the controller
     */
    String path();
}

