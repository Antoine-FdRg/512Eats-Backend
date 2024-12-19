package team.k;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ssdbrestframework.SSDBHttpServer;
import ssdbrestframework.SSDBQueryProcessingException;

public class APIServer {
    public static String getErrorMessage(String body) throws SSDBQueryProcessingException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(body);
            if (rootNode.has("localizedMessage")) {
                String localizedMessage = rootNode.get("localizedMessage").asText();
                return localizedMessage;
            } else {
                System.out.println("Localized Message not found in the JSON string.");
                throw new SSDBQueryProcessingException(500, "Error while parsing the error message", "Localized Message not found in the JSON string.");
            }
        } catch (Exception e) {
            throw new SSDBQueryProcessingException(500, "Error while parsing the error message", e.getMessage());
        }
    }

    public static void main(String[] args) {
        SSDBHttpServer serv = new SSDBHttpServer(8081, "team.k", "./");
        serv.start();
    }
}