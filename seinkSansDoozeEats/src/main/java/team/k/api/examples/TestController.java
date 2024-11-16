package team.k.api.examples;

import team.k.api.annotations.PathVariable;
import team.k.api.annotations.RequestParam;
import team.k.api.annotations.RestController;
import team.k.api.annotations.Endpoint;

@RestController(path = "/greeter")
public class TestController {
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
