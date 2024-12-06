package commonlibrary.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import commonlibrary.dto.databasecreation.RegisteredUserCreatorDTO;
import commonlibrary.model.RegisteredUser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class RegisteredUserRepository {

    private static final String BASE_URL = "http://localhost:8082/registered-users";
    private static final HttpClient client = HttpClient.newHttpClient();

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

    public void add(RegisteredUser registeredUser) throws IOException, InterruptedException {
        RegisteredUserCreatorDTO registeredUserCreatorDTO = new RegisteredUserCreatorDTO(registeredUser.getName(), registeredUser.getRole().getName());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/create"))
                .POST(HttpRequest.BodyPublishers.ofString(new ObjectMapper().writeValueAsString(registeredUserCreatorDTO)))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 300) {
            throw new IOException("Error: " + response.statusCode() + " - " + response.body());
        }
    }

    public RegisteredUser findById(int id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/get/" + id))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 300) {
            throw new IOException("Error: " + response.statusCode() + " - " + response.body());
        }
        return objectMapper.readValue(response.body(), RegisteredUser.class);
    }

    public List<RegisteredUser> findAll() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        if (response.statusCode() >= 300) {
            throw new IOException("Error: " + response.statusCode() + " - " + response.body());

        }
        return List.of(objectMapper.readValue(response.body(), RegisteredUser[].class));
    }

    public void delete(int registeredUserId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/delete/" + registeredUserId))
                .DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 300) {
            throw new IOException("Error: " + response.statusCode() + " - " + response.body());
        }
    }

    public void update(RegisteredUser registeredUser) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/update"))
                .PUT(HttpRequest.BodyPublishers.ofString(new ObjectMapper().writeValueAsString(registeredUser)))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 300) {
            throw new IOException("Error: " + response.statusCode() + " - " + response.body());
        }
    }
}