package commonlibrary.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import commonlibrary.dto.databasecreation.GroupOrderCreatorDTO;
import commonlibrary.dto.databaseupdator.GroupOrderUpdatorDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import commonlibrary.model.order.GroupOrder;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Getter
@NoArgsConstructor
public class GroupOrderRepository {
    private static final String BASE_URL = "http://localhost:8082/group-orders";
    private static final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);


    public GroupOrder findGroupOrderById(int id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/get/" + id))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 300) {
            throw new IOException("Error: " + response.statusCode() + " - " + response.body());
        }
        GroupOrder groupOrder = objectMapper.readValue(response.body(), GroupOrder.class);
        return groupOrder;

    }

    public GroupOrder add(GroupOrder groupOrder) throws IOException, InterruptedException {
        GroupOrderCreatorDTO groupOrderCreatorDTO = new GroupOrderCreatorDTO(groupOrder);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/create"))
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(groupOrderCreatorDTO)))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 300) {
            throw new IOException("Error: " + response.statusCode() + " - " + response.body());
        }
        return objectMapper.readValue(response.body(), GroupOrder.class);
    }

    public GroupOrder update(GroupOrder groupOrder) throws IOException, InterruptedException {
        GroupOrderUpdatorDTO groupOrderUpdatorDTO = new GroupOrderUpdatorDTO(groupOrder);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/update"))
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(groupOrderUpdatorDTO)))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 300) {
            throw new IOException("Error: " + response.statusCode() + " - " + response.body());
        }
        return objectMapper.readValue(response.body(), GroupOrder.class);
    }
}
