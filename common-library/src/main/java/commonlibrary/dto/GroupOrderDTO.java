package commonlibrary.dto;

import commonlibrary.enumerations.OrderStatus;
import commonlibrary.model.Location;
import commonlibrary.model.order.GroupOrder;
import commonlibrary.model.order.SubOrder;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public record GroupOrderDTO(int id, String status, LocationDTO deliveryLocation,
                            String deliveryDateTime, List<SubOrderDTO> suborders) {


    private SubOrder convertSubOrderDtoToSubOrderWithExceptionHandling(SubOrderDTO subOrderDTO) {
        try {
            return subOrderDTO.convertSubOrderDtoToSubOrder();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    /*
     * Converts a GroupOrderDTO object to a GroupOrder object
     */
    public GroupOrder convertGroupOrderDtoToGroupOrder() {
        OrderStatus orderStatus = OrderStatus.valueOf(status);

        Location convertedLocation = deliveryLocation.convertLocationDtoToLocation();

        LocalDateTime deliveryDateTimeParsed = LocalDateTime.parse(deliveryDateTime);
        List<SubOrder> convertedSubOrders = suborders.stream()
                .map(this::convertSubOrderDtoToSubOrderWithExceptionHandling)
                .toList();

        return new GroupOrder.Builder()
                .withId(id)
                .withStatus(orderStatus)
                .withDate(deliveryDateTimeParsed)
                .withDeliveryLocation(convertedLocation)
                .withSubOrders(convertedSubOrders)
                .build();
    }


}
