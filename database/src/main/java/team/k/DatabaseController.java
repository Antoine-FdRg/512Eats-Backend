package team.k;

import ssdbrestframework.HttpMethod;
import ssdbrestframework.annotations.Endpoint;
import ssdbrestframework.annotations.RestController;

@RestController(path = "/db")
public class DatabaseController {

    @Endpoint(path = "/ping", method = HttpMethod.GET)
    public String ping() {
        return "pong";
    }
}
