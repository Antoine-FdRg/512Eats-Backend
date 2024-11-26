package commonlibrary.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import commonlibrary.model.Dish;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;


public class DishRepository {
    private final List<Dish> dishes = new ArrayList<>();
    private static final String BASE_URL = "http://localhost:8082/";
    private static final HttpClient client = HttpClient.newHttpClient();

    public Dish findById(int dishId) {
        return dishes.stream().filter(restaurant -> restaurant.getId() == dishId).findFirst().orElse(null);
    }

    public void add(Dish dish) {
        dishes.add(dish);
    }

    public void remove(Dish dish) {
        dishes.remove(dish);
    }

    public List<Dish> findAll() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "dishes"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        List<Dish> dishes = List.of(new ObjectMapper().readValue(response.body(), Dish[].class)); // throws exception if response is not a valid JSON array
        return dishes;
    }

    public void clear() {
        dishes.clear();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        DishRepository dishRepository = new DishRepository();
        System.out.println(dishRepository.findAll());
    }
}
