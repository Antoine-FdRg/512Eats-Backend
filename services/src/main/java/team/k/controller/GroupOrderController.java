package team.k.controller;

import commonlibrary.dto.GroupOrderDTO;
import commonlibrary.model.order.GroupOrder;
import lombok.RequiredArgsConstructor;
import ssdbrestframework.SSDBQueryProcessingException;
import ssdbrestframework.annotations.Endpoint;
import ssdbrestframework.annotations.PathVariable;
import ssdbrestframework.annotations.RequestParam;
import ssdbrestframework.annotations.Response;
import ssdbrestframework.annotations.RestController;
import team.k.service.GroupOrderService;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@RestController(path = "/group-orders")
public class GroupOrderController {

    private static final String ERROR_GROUP_ORDER_NOT_FOUND = "Group order not found: ";

    /**
     * Create a group order
     *
     * @param deliveryLocationId the location ID where the suborders will be delivered
     * @param deliveryDateTime   the delivery date and time
     * @return the ID of the created group order
     */
    @Endpoint(path = "", method = ssdbrestframework.HttpMethod.POST)
    @Response(status = 201)
    public int createGroupOrder(
            @RequestParam("delivery-location-id") int deliveryLocationId,
            @RequestParam("delivery-date-time") LocalDateTime deliveryDateTime
    ) {
        try {
            return GroupOrderService.createGroupOrder(deliveryLocationId, deliveryDateTime, LocalDateTime.now());
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Location not found: " + deliveryLocationId);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid input: " + e.getMessage());
        }
    }

    /**
     * Find a group order by its ID
     *
     * @param groupOrderId the ID of the group order
     * @return the group order details
     */
    @Endpoint(path = "/get/{groupOrderId}", method = ssdbrestframework.HttpMethod.GET)
    @Response(status = 200)
    public GroupOrderDTO findGroupOrderById(@PathVariable("groupOrderId") int groupOrderId) throws SSDBQueryProcessingException {
        GroupOrder groupOrder = GroupOrderService.findGroupOrderById(groupOrderId);
        if (groupOrder == null) {
            throw new SSDBQueryProcessingException(404, ERROR_GROUP_ORDER_NOT_FOUND + groupOrderId);
        }
        return groupOrder.convertGroupOrderToGroupOrderDto();
    }

    /**
     * Modify a group order's delivery date and time
     *
     * @param groupOrderId     the ID of the group order
     * @param deliveryDateTime the new delivery date and time
     */
    @Endpoint(path = "/modify-delivery-datetime/{groupOrderId}", method = ssdbrestframework.HttpMethod.PUT)
    @Response(status = 204,message = "Group order delivery date and time modified successfully")
    public void modifyGroupOrderDeliveryDateTime(
            @PathVariable("groupOrderId") int groupOrderId,
            @RequestParam("delivery-date-time") LocalDateTime deliveryDateTime
    ) throws SSDBQueryProcessingException {
        try {
            GroupOrderService.modifyGroupOrderDeliveryDateTime(groupOrderId, deliveryDateTime, LocalDateTime.now());
        } catch (NoSuchElementException e) {
            throw new SSDBQueryProcessingException(404, ERROR_GROUP_ORDER_NOT_FOUND + groupOrderId);
        } catch (IllegalArgumentException | UnsupportedOperationException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * Place a group order
     *
     * @param groupOrderId the ID of the group order
     */
    @Endpoint(path = "/place/{groupOrderId}", method = ssdbrestframework.HttpMethod.POST)
    @Response(status = 204)
    public void placeGroupOrder(@PathVariable("groupOrderId") int groupOrderId) {
        try {
            GroupOrderService.place(groupOrderId, LocalDateTime.now());
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(ERROR_GROUP_ORDER_NOT_FOUND + groupOrderId);
        }
    }
}
