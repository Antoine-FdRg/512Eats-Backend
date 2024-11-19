package ssdbrestframework.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation allowing to specify the response status if the method is called successfully,
 * and an optional message to be sent in the response body if the method is void
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Response {
    /**
     * The status code to return in the response
     * @return the status code
     */
    int status();

    /**
     * The message to return in the response body if the method is void
     * @return the message
     */
    String message() default "";
}
