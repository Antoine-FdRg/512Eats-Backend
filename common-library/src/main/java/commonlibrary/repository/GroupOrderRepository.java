package commonlibrary.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
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

    public GroupOrder findGroupOrderById(int id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/get/" + id))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 300) {
            throw new IOException("Error: " + response.statusCode() + " - " + response.body());
        }
        GroupOrder groupOrder = new ObjectMapper().readValue(response.body(), GroupOrder.class);
        return groupOrder;

    }

    public void add(GroupOrder groupOrder) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/create"))
                .POST(HttpRequest.BodyPublishers.ofString(new ObjectMapper().writeValueAsString(groupOrder)))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 300) {
            throw new IOException("Error: " + response.statusCode() + " - " + response.body());
        }
    }
    //TODO : checker toutes les méthodes voir si elles fonctionnent
}
