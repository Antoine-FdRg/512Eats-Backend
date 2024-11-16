package team.k.api.examples;

import lombok.extern.java.Log;
import team.k.api.SSDBServer;
import team.k.api.annotations.RequestBody;
import team.k.api.annotations.RequestParam;
import team.k.api.annotations.RestController;
import team.k.api.annotations.Endpoint;

@RestController(path = "/ciao")
@Log
public class TestController2 {

    @Endpoint(path = "/goodbye")
    public String goodbye() {
        return "Goodbye, World!";
    }

    @Endpoint(path = "/post", method = "POST")
    public MyData post(@RequestBody MyData data) {
        return data;
    }

    @Endpoint(path = "/data", method = "POST")
    public SSDBServer<MyData> data(@RequestParam("name") String name, @RequestParam("age") double age) {
        return SSDBServer.status(201,new MyData(name, age));
    }
}
