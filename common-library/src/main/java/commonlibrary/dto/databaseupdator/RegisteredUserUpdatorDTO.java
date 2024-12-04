package commonlibrary.dto.databaseupdator;

import java.util.List;

public record RegisteredUserUpdatorDTO(int id, String name, String role, Integer currentOrderID, List<Integer> orderIDs) {

}
