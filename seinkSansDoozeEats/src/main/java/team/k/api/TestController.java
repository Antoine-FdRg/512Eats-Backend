package team.k.api;

@RestController(path = "/greeter")
class TestController {
    @WebRoute(path = "/hello/{surname}/{name}")
    public String hello(@PathVariable("name") String name,
                        @PathVariable("surname") String surname,
                        @RequestParam("greeting") String greeting) {
        return greeting + ", " + surname + " " + name + "!";
    }

    @WebRoute(path = "/goodbye")
    public String goodbye() {
        return "Goodbye, World!";
    }
}
