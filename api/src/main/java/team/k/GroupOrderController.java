package team.k;

import commonlibrary.dto.GroupOrderDTO;
import ssdbrestframework.HttpMethod;
import ssdbrestframework.SSDBQueryProcessingException;
import ssdbrestframework.annotations.ApiResponseExample;
import ssdbrestframework.annotations.Endpoint;
import ssdbrestframework.annotations.PathVariable;
import ssdbrestframework.annotations.RequestParam;
import ssdbrestframework.annotations.Response;
import ssdbrestframework.annotations.RestController;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@RestController(path = "/group-orders")
public class GroupOrderController {
    private static final String GROUPORDER_SERVICE_URL = "http://localhost:8083/group-orders";

    @Endpoint(path = "", method = HttpMethod.POST)
    @ApiResponseExample(value = int.class) //TODO @Clement check why this is not working well in the swagger
    public int createGroupOrder(
            @RequestParam("delivery-location-id") String deliveryLocationId,
            @RequestParam("delivery-date-time") String deliveryDateTime
    ) throws URISyntaxException, IOException, InterruptedException, SSDBQueryProcessingException {
        if (deliveryLocationId == null) {
            throw new SSDBQueryProcessingException(400, "Missing delivery location ID");
        }
        String params = "?delivery-location-id=" + deliveryLocationId;
        if (deliveryDateTime != null) {
            params += "&delivery-date-time=" + deliveryDateTime;
        }
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(GROUPORDER_SERVICE_URL + params))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 201) {
            throw new SSDBQueryProcessingException(response.statusCode(), response.body());
        }
        return Integer.parseInt(response.body());
    }

    @Endpoint(path = "/get/{groupOrderId}", method = HttpMethod.GET)
    @ApiResponseExample(value = GroupOrderDTO.class)
    public String findGroupOrderById(@PathVariable("groupOrderId") String groupOrderId) throws URISyntaxException, IOException, InterruptedException, SSDBQueryProcessingException {
        if (groupOrderId == null) {
            throw new SSDBQueryProcessingException(400, "Missing group order ID");
        }
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(GROUPORDER_SERVICE_URL + "/get/" + groupOrderId))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new SSDBQueryProcessingException(response.statusCode(), response.body());
        }
        return response.body();
    }

    @Endpoint(path = "/modify-delivery-datetime/{groupOrderId}", method = HttpMethod.PUT)
    @Response(status = 204, message = "Group order delivery date and time modified successfully")
    @ApiResponseExample(value = void.class)
    public void modifyGroupOrderDeliveryDateTime(
            @PathVariable("groupOrderId") String groupOrderId,
            @RequestParam("delivery-date-time") String deliveryDateTime
    ) throws URISyntaxException, IOException, InterruptedException, SSDBQueryProcessingException {
        if (groupOrderId == null) {
            throw new SSDBQueryProcessingException(400, "Missing group order ID");
        }
        if (deliveryDateTime == null) {
            throw new SSDBQueryProcessingException(400, "Missing delivery date and time");
        }
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(GROUPORDER_SERVICE_URL + "/modify-delivery-datetime/" + groupOrderId + "?delivery-date-time=" + deliveryDateTime))
                .PUT(HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 204) {
            throw new SSDBQueryProcessingException(response.statusCode(), response.body());
        }
    }

    @Endpoint(path = "/place/{groupOrderId}", method = HttpMethod.POST)
    @Response(status = 204)
    @ApiResponseExample(value = void.class)
    public void placeGroupOrder(@PathVariable("groupOrderId") String groupOrderId) throws URISyntaxException, IOException, InterruptedException, SSDBQueryProcessingException {
        if (groupOrderId == null) {
            throw new SSDBQueryProcessingException(400, "Missing group order ID");
        }
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(GROUPORDER_SERVICE_URL + "/place/" + groupOrderId))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 204) {
            throw new SSDBQueryProcessingException(response.statusCode(), response.body());
        }
    }
}
