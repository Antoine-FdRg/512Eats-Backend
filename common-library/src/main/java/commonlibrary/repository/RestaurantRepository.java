package commonlibrary.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import commonlibrary.dto.databasecreation.RestaurantCreatorDTO;
import commonlibrary.dto.databaseupdator.RestauranUpdatorDTO;
import commonlibrary.enumerations.FoodType;
import commonlibrary.model.restaurant.Restaurant;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.List;

public class RestaurantRepository {
    private static final String BASE_URL = "http://localhost:8082/restaurants";
    private static final HttpClient client = HttpClient.newHttpClient();

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

    private RestaurantRepository() {
    }

    /**
     * Find a restaurant by its id.
     *
     * @param id the id of the restaurant
     * @return the restaurant if found, null otherwise
     */
    public static Restaurant findById(int id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/get/" + id))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 300) {
            throw new IOException("Error: " + response.statusCode() + " - " + response.body());
        }
        Restaurant restaurant = objectMapper.readValue(response.body(), Restaurant.class);
        return restaurant;
    }

    /**
     * Find all restaurants.
     *
     * @return the list of all restaurants
     */
    public static List<Restaurant> findAll() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 300) {
            throw new IOException("Error: " + response.statusCode() + " - " + response.body());
        }
        List<Restaurant> restaurants = List.of(objectMapper.readValue(response.body(), Restaurant[].class)); // throws exception if response is not a valid JSON array
        return restaurants;
    }

    /**
     * Add a restaurant to the repository.
     *
     * @param restaurant the restaurant to add
     */
    public static Restaurant add(Restaurant restaurant) throws IOException, InterruptedException {
        RestaurantCreatorDTO restaurantCreatorDTO = new RestaurantCreatorDTO(restaurant);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/create"))
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(restaurantCreatorDTO)))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 300) {
            throw new IOException("Error: " + response.statusCode() + " - " + response.body());
        }
        return objectMapper.readValue(response.body(), Restaurant.class);
    }

    /**
     * Delete a restaurant from the repository.
     *
     * @param restaurantId the restaurant to delete
     */
    public static void delete(int restaurantId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/delete/" + restaurantId))
                .DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 300) {
            throw new IOException("Error: " + response.statusCode() + " - " + response.body());
        }
    }

    public static List<Restaurant> findRestaurantByFoodType(List<FoodType> foodTypes) throws IOException, InterruptedException {
        List<String> foodTypesString = foodTypes.stream().map(Enum::name).toList();
        String foodTypesStringJoined = String.join(",", foodTypesString);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/get/foodtypes?food-types=" + foodTypesStringJoined))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 300) {
            throw new IOException("Error: " + response.statusCode() + " - " + response.body());
        }
        List<Restaurant> restaurants = List.of(objectMapper.readValue(response.body(), Restaurant[].class));
        return restaurants;
    }

    public static List<Restaurant> findRestaurantsByAvailability(LocalDateTime timeChosen) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/by/availability?date=" + timeChosen))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 300) {
            throw new IOException("Error: " + response.statusCode() + " - " + response.body());
        }
        List<Restaurant> restaurants = List.of(objectMapper.readValue(response.body(), Restaurant[].class));
        return restaurants;

    }

    public static List<Restaurant> findRestaurantByName(String name) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "?name=" + name))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 300) {
            throw new IOException("Error: " + response.statusCode() + " - " + response.body());
        }
        List<Restaurant> restaurants = List.of(objectMapper.readValue(response.body(), Restaurant[].class)); // throws exception if response is not a valid JSON array
        return restaurants;
    }

    public static Restaurant update(Restaurant restaurant) throws IOException, InterruptedException {
        RestauranUpdatorDTO restauranUpdatorDTO = new RestauranUpdatorDTO(restaurant);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/update"))
                .PUT(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(restauranUpdatorDTO)))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 300) {
            throw new IOException("Error: " + response.statusCode() + " - " + response.body());
        }
        return objectMapper.readValue(response.body(), Restaurant.class);
    }


}
