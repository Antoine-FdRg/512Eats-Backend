package team.k;

import commonlibrary.model.Dish;
import ssdbrestframework.HttpMethod;
import ssdbrestframework.SSDBQueryProcessingException;
import ssdbrestframework.annotations.ApiResponseExample;
import ssdbrestframework.annotations.Endpoint;
import ssdbrestframework.annotations.RequestBody;
import ssdbrestframework.annotations.RequestParam;
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

        if (response.statusCode() > 204) {
            throw new SSDBQueryProcessingException(response.statusCode(), response.body());
        }

        return response.body();
    }

    @Endpoint(path = "/dishes", method = HttpMethod.GET)
    @ApiResponseExample(value= Dish.class, isArray = true)
    public String getDishes(@RequestParam("order-id") String orderId) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(ORDER_SERVICE_URL + "/dishes?order-id=" + orderId))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() > 204) {
            throw new SSDBQueryProcessingException(response.statusCode(), response.body());
        }

        return response.body();
    }

    @Endpoint(path = "/available-dishes", method = HttpMethod.GET)
    @ApiResponseExample(value= Dish.class, isArray = true)
    public String getAvailableDishes(@RequestParam("order-id") String orderId) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(ORDER_SERVICE_URL + "/available-dishes?order-id=" + orderId))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() > 204) {
            throw new SSDBQueryProcessingException(response.statusCode(), response.body());
        }

        return response.body();
    }

    @Endpoint(path = "/pay", method = HttpMethod.POST)
    public String pay(@RequestBody String payRequest) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(ORDER_SERVICE_URL + "/pay"))
                .POST(HttpRequest.BodyPublishers.ofString(payRequest))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() > 204) {
            throw new SSDBQueryProcessingException(response.statusCode(), response.body());
        }

        return response.body();
    }

    @Endpoint(path = "/place", method = HttpMethod.POST)
    public String place(@RequestBody String placeRequest) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(ORDER_SERVICE_URL + "/place"))
                .POST(HttpRequest.BodyPublishers.ofString(placeRequest))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() > 204) {
            throw new SSDBQueryProcessingException(response.statusCode(), response.body());
        }

        return response.body();
    }

    @Endpoint(path = "/sub-order", method = HttpMethod.POST)
    public String createSubOrder(@RequestBody String createSubOrderRequest) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(ORDER_SERVICE_URL + "/sub-order"))
                .POST(HttpRequest.BodyPublishers.ofString(createSubOrderRequest))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() > 204) {
            throw new SSDBQueryProcessingException(response.statusCode(), response.body());
        }

        return response.body();
    }

}