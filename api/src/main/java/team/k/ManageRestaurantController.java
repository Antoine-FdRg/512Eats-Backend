package team.k;

import commonlibrary.dto.RestaurantDTO;
import ssdbrestframework.HttpMethod;
import ssdbrestframework.SSDBQueryProcessingException;
import ssdbrestframework.annotations.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static team.k.APIServer.getErrorMessage;

@RestController(path = "/management")
public class ManageRestaurantController {
    private static final String MANAGE_RESTAURANT_SERVICE_URL = "http://localhost:8083/manage-restaurant";

    @Endpoint(path = "/update-restaurant-infos", method = HttpMethod.POST)
    @ApiResponseExample(value = RestaurantDTO.class)
    public String updateRestaurantInfos(@RequestParam("restaurant-id") int restaurantId, @RequestBody String managingRestaurantDTO) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(MANAGE_RESTAURANT_SERVICE_URL + "/update-restaurant-infos?restaurant-id=" + restaurantId))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(managingRestaurantDTO))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() > 204) {
            throw new SSDBQueryProcessingException(response.statusCode(), getErrorMessage(response.body()));
        }

        return response.body();
    }

    @Endpoint(path = "/add-dish", method = HttpMethod.POST)
    @ApiResponseExample(value = RestaurantDTO.class)
    public String addDish(@RequestParam("restaurant-id") int restaurantId, @RequestBody String dishDTO) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(MANAGE_RESTAURANT_SERVICE_URL + "/add-dish?restaurant-id=" + restaurantId))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(dishDTO))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() > 204) {
            throw new SSDBQueryProcessingException(response.statusCode(), getErrorMessage(response.body()));
        }

        return response.body();
    }

    @Endpoint(path = "/remove-dish", method = HttpMethod.DELETE)
    @ApiResponseExample(value = RestaurantDTO.class)
    public String removeDish(@RequestParam("restaurant-id") int restaurantId, @RequestParam("dish-id") int dishId) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(MANAGE_RESTAURANT_SERVICE_URL + "/remove-dish?restaurant-id=" + restaurantId + "&dish-id=" + dishId))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() > 204) {
            throw new SSDBQueryProcessingException(response.statusCode(), getErrorMessage(response.body()));
        }

        return response.body();
    }

    @Endpoint(path = "/update-dish", method = HttpMethod.PUT)
    @ApiResponseExample(value = RestaurantDTO.class)
    public String updateDish(@RequestParam("restaurant-id") int restaurantId, @RequestBody String dishDTO) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(MANAGE_RESTAURANT_SERVICE_URL + "/update-dish?restaurant-id=" + restaurantId))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(dishDTO))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() > 204) {
            throw new SSDBQueryProcessingException(response.statusCode(), getErrorMessage(response.body()));
        }

        return response.body();
    }
}
