package commonlibrary.dto;

import commonlibrary.enumerations.OrderStatus;
import commonlibrary.model.order.GroupOrder;
import commonlibrary.model.order.SubOrder;

import java.time.LocalDateTime;
import java.util.List;

public record GroupOrderDTO(int id, String status, int deliveryLocationID,
                            String deliveryDateTime, List<SubOrderDTO> suborders) {

    /*
     * Converts a GroupOrderDTO object to a GroupOrder object
     */
    public GroupOrder convertGroupOrderDtoToGroupOrder() {
        OrderStatus orderStatus = OrderStatus.valueOf(status);

        LocalDateTime deliveryDateTimeParsed = LocalDateTime.parse(deliveryDateTime);
        List<SubOrder> convertedSubOrders = suborders.stream()
                .map(SubOrderDTO::convertSubOrderDtoToSubOrder)
                .toList();

        return new GroupOrder.Builder()
                .withId(id)
                .withStatus(orderStatus)
                .withDate(deliveryDateTimeParsed)
                .withDeliveryLocationID(deliveryLocationID)
                .withSubOrders(convertedSubOrders)
                .build();
    }


}
