package team.k;

import ssdbrestframework.annotations.Endpoint;
import ssdbrestframework.annotations.RestController;

@RestController(path = "/api")
public class APIController {

    @Endpoint(path = "/ping")
    public String ping() {
        return "pong";
    }
}
