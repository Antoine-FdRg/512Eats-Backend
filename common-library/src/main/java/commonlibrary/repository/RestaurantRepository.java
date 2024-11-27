package commonlibrary.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
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

    /**
     * Find a restaurant by its id.
     *
     * @param id the id of the restaurant
     * @return the restaurant if found, null otherwise
     */
    public Restaurant findById(int id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/get/" + id))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 300) {
            throw new IOException("Error: " + response.statusCode() + " - " + response.body());
        }
        Restaurant restaurant = new ObjectMapper().readValue(response.body(), Restaurant.class);
        return restaurant;
    }

    /**
     * Find all restaurants.
     *
     * @return the list of all restaurants
     */
    public List<Restaurant> findAll() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 300) {
            throw new IOException("Error: " + response.statusCode() + " - " + response.body());
        }
        List<Restaurant> restaurants = List.of(new ObjectMapper().readValue(response.body(), Restaurant[].class)); // throws exception if response is not a valid JSON array
        return restaurants;
    }

    /**
     * Add a restaurant to the repository.
     *
     * @param restaurant the restaurant to add
     */
    public void add(Restaurant restaurant) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/create"))
                .POST(HttpRequest.BodyPublishers.ofString(new ObjectMapper().writeValueAsString(restaurant)))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 300) {
            throw new IOException("Error: " + response.statusCode() + " - " + response.body());
        }
    }

    /**
     * Delete a restaurant from the repository.
     *
     * @param restaurantId the restaurant to delete
     */
    public void delete(int restaurantId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/delete/" + restaurantId))
                .DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 300) {
            throw new IOException("Error: " + response.statusCode() + " - " + response.body());
        }
    }

    public List<Restaurant> findRestaurantByFoodType(List<FoodType> foodTypes) throws IOException, InterruptedException {
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
        List<Restaurant> restaurants = List.of(new ObjectMapper().readValue(response.body(), Restaurant[].class));
        return restaurants;
    }

    public List<Restaurant> findRestaurantsByAvailability(LocalDateTime timeChosen) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/by/availability?date=" + timeChosen))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 300) {
            throw new IOException("Error: " + response.statusCode() + " - " + response.body());
        }
        List<Restaurant> restaurants = List.of(new ObjectMapper().readValue(response.body(), Restaurant[].class));
        return restaurants;

    }

    public List<Restaurant> findRestaurantByName(String name) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "name?name=" + name))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 300) {
            throw new IOException("Error: " + response.statusCode() + " - " + response.body());
        }
        List<Restaurant> restaurants = List.of(new ObjectMapper().readValue(response.body(), Restaurant[].class)); // throws exception if response is not a valid JSON array
        return restaurants;
    }


}
