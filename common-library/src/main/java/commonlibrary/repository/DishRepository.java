package commonlibrary.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import commonlibrary.dto.databasecreation.DishCreatorDTO;
import commonlibrary.model.Dish;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;


public class DishRepository {
    private static final String BASE_URL = "http://localhost:8082/dishes";
    private static final HttpClient client = HttpClient.newHttpClient();

    public Dish findById(int dishId) throws IOException, InterruptedException {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/get/" + dishId))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() >= 300) {
                throw new IOException("Error: " + response.statusCode() + " - " + response.body());
            }
        return new ObjectMapper().readValue(response.body(), Dish.class);
    }

    public Dish add(Dish dish) throws IOException, InterruptedException {
        DishCreatorDTO dishCreatorDTO = new DishCreatorDTO(dish.getName(), dish.getDescription(), dish.getPrice(), dish.getPreparationTime(), dish.getPicture());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/create"))
                .POST(HttpRequest.BodyPublishers.ofString(new ObjectMapper().writeValueAsString(dishCreatorDTO)))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 300) {
            throw new IOException("Error: " + response.statusCode() + " - " + response.body());
        }
        return new ObjectMapper().readValue(response.body(), Dish.class);
    }

    public void remove(int id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/delete/" + id))
                .DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 300) {
            throw new IOException("Error: " + response.statusCode() + " - " + response.body());
        }
    }

    public List<Dish> findAll() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        if (response.statusCode() >= 300) {
            throw new IOException("Error: " + response.statusCode() + " - " + response.body());

        }
        // throws exception if response is not a valid JSON array
        return List.of(new ObjectMapper().readValue(response.body(), Dish[].class));

    }


}
