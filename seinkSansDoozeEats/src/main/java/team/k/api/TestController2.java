package team.k.api;

@RestController(path = "/ciao")
class TestController2 {

    @WebRoute(path = "/goodbye")
    public String goodbye() {
        return "Goodbye, World!";
    }

    @WebRoute(path = "/post", method = "POST")
    public String handlePostData(@RequestBody MyData data) {
        return "Received data: " + data.toString();
    }
}
