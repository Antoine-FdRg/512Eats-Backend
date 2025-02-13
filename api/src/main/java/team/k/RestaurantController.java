package team.k;

import commonlibrary.dto.RestaurantDTO;
import commonlibrary.enumerations.FoodType;
import commonlibrary.model.Dish;
import ssdbrestframework.HttpMethod;
import ssdbrestframework.SSDBQueryProcessingException;
import ssdbrestframework.annotations.ApiResponseExample;
import ssdbrestframework.annotations.Endpoint;
import ssdbrestframework.annotations.RequestParam;
import ssdbrestframework.annotations.RestController;

import java.net.http.HttpClient;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static team.k.APIServer.getErrorMessage;

@RestController(path = "/restaurants")
public class RestaurantController {
    private static final String RESTAURANT_SERVICE_URL = "http://localhost:8083/restaurants";

    @Endpoint(path = "/food-types", method = HttpMethod.GET)
    @ApiResponseExample(value = FoodType.class, isArray = true)
    public String getFoodTypes() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(RESTAURANT_SERVICE_URL + "/food-types"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() > 204) {
            throw new SSDBQueryProcessingException(response.statusCode(), getErrorMessage(response.body()));
        }
        return response.body();
    }

    @Endpoint(path = "", method = HttpMethod.GET)
    @ApiResponseExample(value = RestaurantDTO.class, isArray = true)
    public String getRestaurants() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(RESTAURANT_SERVICE_URL))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() > 204) {
            throw new SSDBQueryProcessingException(response.statusCode(), getErrorMessage(response.body()));
        }

        return response.body();
    }

    //TODO fix this of the services endpoint
    @Endpoint(path = "/by", method = HttpMethod.GET)
    @ApiResponseExample(value = RestaurantDTO.class, isArray = true)
    public String getRestaurantBy(
            @RequestParam("availability") String availability,
            @RequestParam("food-types") String foodTypes,
            @RequestParam("name") String name
    ) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        String parameter = "";
        if (availability != null) {
            parameter += "/by/availability";
        } else if (foodTypes != null) {
            parameter += "/by/food-types?food-types=" + foodTypes;
        } else if (name != null) {
            parameter += "/by/name/" + name;
        } else {
            throw new IllegalArgumentException("No parameter provided");
        }
        System.out.println("Going to " + RESTAURANT_SERVICE_URL + parameter);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(RESTAURANT_SERVICE_URL + parameter))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() > 204) {
            throw new SSDBQueryProcessingException(response.statusCode(), getErrorMessage(response.body()));
        }
        return response.body();
    }

    @Endpoint(path = "/dishes", method = HttpMethod.GET)
    @ApiResponseExample(value = Dish.class, isArray = true)
    public String getDishes(@RequestParam("restaurant-id") int restaurantId) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(RESTAURANT_SERVICE_URL + "/dishes?restaurant-id=" + restaurantId))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() > 204) {
            throw new SSDBQueryProcessingException(response.statusCode(), getErrorMessage(response.body()));
        }

        return response.body();
    }
}