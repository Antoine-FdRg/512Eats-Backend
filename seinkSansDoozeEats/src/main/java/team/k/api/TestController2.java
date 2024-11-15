package team.k.api;

import lombok.extern.java.Log;

@RestController(path = "/ciao")
@Log
class TestController2 {

    @WebRoute(path = "/goodbye")
    public String goodbye() {
        return "Goodbye, World!";
    }

    @WebRoute(path = "/post", method = "POST")
    public MyData post(@RequestBody MyData data) {
        return data;
    }

    @WebRoute(path = "/data", method = "POST")
    public Response<MyData> data(@RequestParam("name") String name, @RequestParam("age") double age) {
        return Response.status(201,new MyData(name, age));
    }
}
