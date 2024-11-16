package team.k.api.examples;

import lombok.extern.java.Log;
import team.k.api.Response;
import team.k.api.annotations.RequestBody;
import team.k.api.annotations.RequestParam;
import team.k.api.annotations.RestController;
import team.k.api.annotations.Endpoint;

@RestController(path = "/ciao")
@Log
class TestController2 {

    @Endpoint(path = "/goodbye")
    public String goodbye() {
        return "Goodbye, World!";
    }

    @Endpoint(path = "/post", method = "POST")
    public MyData post(@RequestBody MyData data) {
        return data;
    }

    @Endpoint(path = "/data", method = "POST")
    public Response<MyData> data(@RequestParam("name") String name, @RequestParam("age") double age) {
        return Response.status(201,new MyData(name, age));
    }
}
