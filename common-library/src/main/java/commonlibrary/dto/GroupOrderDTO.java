package commonlibrary.dto;

import java.util.List;

public record GroupOrderDTO(int id, String status, int deliveryLocationID,
                            String deliveryDateTime, List<SubOrderDTO> suborders) {
}
