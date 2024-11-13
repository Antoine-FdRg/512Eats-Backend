package team.k.api;

@RestController(path = "/ciao")
class TestController2 {

    @WebRoute(path = "/goodbye")
    public String goodbye() {
        return "Goodbye, World!";
    }
}
