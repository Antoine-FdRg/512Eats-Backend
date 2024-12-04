package commonlibrary.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import commonlibrary.dto.databasecreation.SubOrderCreatorDTO;
import commonlibrary.dto.databaseupdator.SubOrderUpdatorDTO;
import commonlibrary.model.Dish;
import commonlibrary.model.order.SubOrder;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class SubOrderRepository {

    private static final String BASE_URL = "http://localhost:8082/sub-orders";
    private static final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);


    public SubOrder add(SubOrder subOrder) throws IOException, InterruptedException {
        SubOrderCreatorDTO subOrderCreatorDTO = new SubOrderCreatorDTO(subOrder);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/create"))
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(subOrderCreatorDTO)))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 300) {
            throw new IOException("Error: " + response.statusCode() + " - " + response.body());
        }
        return objectMapper.readValue(response.body(), SubOrder.class);
    }

    public SubOrder findById(int id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/get/" + id))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 300) {
            throw new IOException("Error: " + response.statusCode() + " - " + response.body());
        }
        SubOrder subOrder = objectMapper.readValue(response.body(), SubOrder.class);
        return subOrder;
    }

    public List<SubOrder> findAll() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        if (response.statusCode() >= 300) {
            throw new IOException("Error: " + response.statusCode() + " - " + response.body());

        }
        List<SubOrder> subOrders = List.of(objectMapper.readValue(response.body(), SubOrder[].class)); // throws exception if response is not a valid JSON array
        return subOrders;
    }

    public void delete(int subOrderId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/delete/" + subOrderId))
                .DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 300) {
            throw new IOException("Error: " + response.statusCode() + " - " + response.body());
        }
    }

    public SubOrder findByUserId(int id) throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "?userId=" + id))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        if (response.statusCode() >= 300) {
            throw new IOException("Error: " + response.statusCode() + " - " + response.body());

        }
        SubOrder subOrder = objectMapper.readValue(response.body(), SubOrder.class); // throws exception if response is not a valid JSON array
        return subOrder;
    }

    public void update(SubOrder subOrder) throws IOException, InterruptedException {
        SubOrderUpdatorDTO subOrderUpdatorDTO = new SubOrderUpdatorDTO(subOrder);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/update"))
                .PUT(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(subOrderUpdatorDTO)))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 300) {
            throw new IOException("Error: " + response.statusCode() + " - " + response.body());
        }
    }
}
