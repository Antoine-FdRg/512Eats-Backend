package ssdbrestframework.examples;

import ssdbrestframework.annotations.PathVariable;
import ssdbrestframework.annotations.RequestParam;
import ssdbrestframework.annotations.RestController;
import ssdbrestframework.annotations.Endpoint;

@RestController(path = "/greeter")
public class ExampleController {
    @Endpoint(path = "/hello/{surname}/{name}")
    public String hello(@PathVariable("name") String name,
                        @PathVariable("surname") String surname,
                        @RequestParam("greeting") String greeting) {
        return greeting + ", " + surname + " " + name + "!";
    }

    @Endpoint(path = "/goodbye", method = "POST")
    public String goodbye(@RequestParam("name") String name) {
        return "Goodbye, "+ name +"!";
    }
}
