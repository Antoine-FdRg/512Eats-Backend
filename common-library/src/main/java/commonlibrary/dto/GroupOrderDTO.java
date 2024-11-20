package commonlibrary.dto;

import java.util.List;

public record GroupOrderDTO(int id, String status, String placedDate, LocationDTO deliveryLocation,
                            String deliveryDateTime, List<SubOrderDTO> suborders) {
}
