package ssdbrestframework.examples;

import lombok.extern.java.Log;
import ssdbrestframework.HttpMethod;
import ssdbrestframework.annotations.RequestBody;
import ssdbrestframework.annotations.RequestParam;
import ssdbrestframework.annotations.Response;
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
    @Response(status=201, message="Data created with success")
    public void post(@RequestBody ExampleData data) {
        log.info("received : " + data.toString());
    }

    @Endpoint(path = "/data", method = HttpMethod.POST)
    @Response(status=201)
    public ExampleData data(@RequestParam("name") String name, @RequestParam("age") double age) {
        return new ExampleData(name, age);
    }
}
