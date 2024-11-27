package team.k;

import ssdbrestframework.HttpMethod;
import ssdbrestframework.annotations.Endpoint;
import ssdbrestframework.annotations.RequestBody;
import ssdbrestframework.annotations.RestController;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@RestController(path = "/orders")
public class OrderController {
    private static final String ORDER_SERVICE_URL = "http://localhost:8083/orders";


    @Endpoint(path = "/add-dish", method = HttpMethod.POST)
    public String addDish(@RequestBody String addDishRequest) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(ORDER_SERVICE_URL + "/add-dish"))
                .POST(HttpRequest.BodyPublishers.ofString(addDishRequest))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed to add dish: " + response.statusCode());
        }

        return response.body();
    }

}