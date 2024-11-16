package team.k.api.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation for defining a request body, must be used in conjunction with {@link Endpoint}, just before a parameter that is the request body
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestBody {
}
