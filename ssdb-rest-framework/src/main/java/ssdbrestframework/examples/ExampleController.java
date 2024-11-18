package ssdbrestframework.examples;

import ssdbrestframework.HttpMethod;
import ssdbrestframework.annotations.PathVariable;
import ssdbrestframework.annotations.RequestParam;
import ssdbrestframework.annotations.Response;
import ssdbrestframework.annotations.RestController;
import ssdbrestframework.annotations.Endpoint;

@RestController(path = "/greeter")
public class ExampleController {
    @Endpoint(path = "/hello/{surname}/{name}", method = HttpMethod.GET)
    public String hello(@PathVariable("name") String name,
                        @PathVariable("surname") String surname,
                        @RequestParam("greeting") String greeting) {
        return greeting + ", " + surname + " " + name + "!";
    }

    @Endpoint(path = "/goodbye", method = HttpMethod.POST)
    public String goodbye(@RequestParam("name") String name) {
        return "Goodbye, " + name + "!";
    }

    @Endpoint(path="/get-int", method = HttpMethod.GET)
    @Response(status = 203)
    public int test(){
        return 23;
    }
}
