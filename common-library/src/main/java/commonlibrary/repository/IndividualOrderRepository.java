package commonlibrary.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import commonlibrary.model.order.IndividualOrder;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class IndividualOrderRepository {

    private static final String BASE_URL = "http://localhost:8082/individual-orders";
    private static final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);



    public void add(IndividualOrder individualOrder) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/create"))
                .POST(HttpRequest.BodyPublishers.ofString(new ObjectMapper().writeValueAsString(individualOrder)))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 300) {
            throw new IOException("Error: " + response.statusCode() + " - " + response.body());
        }
    }

    public List<IndividualOrder> findAll() throws IOException, InterruptedException {
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
        return List.of(objectMapper.readValue(response.body(), IndividualOrder[].class));
    }

    public IndividualOrder findById(int id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "get" + id))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        if (response.statusCode() >= 300) {
            throw new IOException("Error: " + response.statusCode() + " - " + response.body());

        }
        // throws exception if response is not a valid JSON array
        return objectMapper.readValue(response.body(), IndividualOrder.class);
    }

    public void delete(int individualOrderId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/delete/" + individualOrderId))
                .DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 300) {
            throw new IOException("Error: " + response.statusCode() + " - " + response.body());
        }
    }


}//TODO : checker toutes les méthodes voir si elles fonctionnent