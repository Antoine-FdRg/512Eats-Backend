package ssdbrestframework.examples;

import lombok.extern.java.Log;
import ssdbrestframework.HttpMethod;
import ssdbrestframework.SSDBResponse;
import ssdbrestframework.annotations.RequestBody;
import ssdbrestframework.annotations.RequestParam;
import ssdbrestframework.annotations.RestController;
import ssdbrestframework.annotations.Endpoint;

@RestController(path = "/ciao")
@Log
public class ExampleController2 {

    @Endpoint(path = "/goodbye", method = HttpMethod.GET)
    public String goodbye() {
        return "Goodbye, World!";
    }

    @Endpoint(path = "/post", method = HttpMethod.POST)
    public ExampleData post(@RequestBody ExampleData data) {
        return data;
    }

    @Endpoint(path = "/data", method = HttpMethod.POST)
    public SSDBResponse<ExampleData> data(@RequestParam("name") String name, @RequestParam("age") double age) {
        return SSDBResponse.status(201, new ExampleData(name, age));
    }
}
