package ssdbrestframework.examples;

import ssdbrestframework.HttpMethod;
import ssdbrestframework.annotations.Endpoint;
import ssdbrestframework.annotations.PathVariable;
import ssdbrestframework.annotations.RestController;

@RestController(path = "/dish")
public class ExampleController3 {
    @Endpoint(path = "/", method = HttpMethod.GET)
    public String hello() {
        return "Hello !";
    }

    @Endpoint(path = "/{name}", method = HttpMethod.GET)
    public String hello2(@PathVariable("name") String name) {
        return "Hello AGAIN " + name + "!";
    }
}
