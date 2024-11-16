package team.k;

import ssdbrestframework.annotations.Endpoint;
import ssdbrestframework.annotations.RestController;

@RestController(path = "/db")
public class DatabaseController {

    @Endpoint(path = "/ping")
    public String ping() {
        return "pong";
    }
}
