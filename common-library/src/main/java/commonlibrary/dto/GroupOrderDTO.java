package commonlibrary.dto;

import commonlibrary.model.Location;

import java.util.List;

public record GroupOrderDTO(int id, String status, Location deliveryLocation,
                            String deliveryDateTime, List<SubOrderDTO> suborders) {
}
