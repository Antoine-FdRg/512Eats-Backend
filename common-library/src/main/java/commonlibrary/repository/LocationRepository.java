package commonlibrary.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import commonlibrary.dto.databasecreation.LocationCreatorDTO;
import commonlibrary.model.Dish;
import commonlibrary.model.Location;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class LocationRepository {

    private static final String BASE_URL = "http://localhost:8082/locations";
    private static final HttpClient client = HttpClient.newHttpClient();

    /**
     * Returns a location by its id.
     *
     * @param id the id of the location to return.
     * @return the location with the given id, or null if no such location exists.
     */
    public Location findLocationById(int id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/get/" + id))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 300) {
            throw new IOException("Error: " + response.statusCode() + " - " + response.body());
        }
        Location location = new ObjectMapper().readValue(response.body(), Location.class);
        return location;
    }

    public Location add(Location location) throws IOException, InterruptedException {
        LocationCreatorDTO locationCreatorDTO = new LocationCreatorDTO(location);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/create"))
                .POST(HttpRequest.BodyPublishers.ofString(new ObjectMapper().writeValueAsString(locationCreatorDTO)))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 300) {
            throw new IOException("Error: " + response.statusCode() + " - " + response.body());
        }
        return new ObjectMapper().readValue(response.body(), Location.class);
    }

    //TODO : checker toutes les méthodes voir si elles fonctionnent
}
