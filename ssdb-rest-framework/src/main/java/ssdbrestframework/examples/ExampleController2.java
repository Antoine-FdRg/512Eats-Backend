package ssdbrestframework.examples;

import lombok.extern.java.Log;
import ssdbrestframework.SSDBServer;
import ssdbrestframework.annotations.RequestBody;
import ssdbrestframework.annotations.RequestParam;
import ssdbrestframework.annotations.RestController;
import ssdbrestframework.annotations.Endpoint;

@RestController(path = "/ciao")
@Log
public class ExampleController2 {

    @Endpoint(path = "/goodbye")
    public String goodbye() {
        return "Goodbye, World!";
    }

    @Endpoint(path = "/post", method = "POST")
    public ExampleData post(@RequestBody ExampleData data) {
        return data;
    }

    @Endpoint(path = "/data", method = "POST")
    public SSDBServer<ExampleData> data(@RequestParam("name") String name, @RequestParam("age") double age) {
        return SSDBServer.status(201,new ExampleData(name, age));
    }
}
