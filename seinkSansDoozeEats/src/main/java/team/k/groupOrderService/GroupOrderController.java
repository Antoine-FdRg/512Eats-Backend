
package team.k.groupOrderService;

import commonlibrary.dto.GroupOrderDTO;
import commonlibrary.model.order.GroupOrder;
import lombok.RequiredArgsConstructor;
import ssdbrestframework.annotations.Endpoint;
import ssdbrestframework.annotations.PathVariable;
import ssdbrestframework.annotations.RequestParam;
import ssdbrestframework.annotations.Response;
import ssdbrestframework.annotations.RestController;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@RestController(path = "/group-orders")
public class GroupOrderController {

    private final GroupOrderService groupOrderService;

    /**
     * Create a group order
     *
     * @param deliveryLocationId the location ID where the suborders will be delivered
     * @param deliveryDateTime   the delivery date and time
     * @return the ID of the created group order
     */
    @Endpoint(path = "/", method = ssdbrestframework.HttpMethod.POST)
    @Response(status = 201)
    public int createGroupOrder(
            @RequestParam("deliveryLocationId") int deliveryLocationId,
            @RequestParam("deliveryDateTime") LocalDateTime deliveryDateTime
    ) {
        try {
            return groupOrderService.createGroupOrder(deliveryLocationId, deliveryDateTime, LocalDateTime.now());
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
    @Endpoint(path = "/{groupOrderId}", method = ssdbrestframework.HttpMethod.GET)
    @Response(status = 200)
    public GroupOrderDTO findGroupOrderById(@PathVariable("groupOrderId") int groupOrderId) {
        GroupOrder groupOrder = groupOrderService.findGroupOrderById(groupOrderId);
        if (groupOrder == null) {
            throw new NoSuchElementException("Group order not found: " + groupOrderId);
        }
        return groupOrder.convertGroupOrderToGroupOrderDto();
    }

    /**
     * Modify a group order's delivery date and time
     *
     * @param groupOrderId     the ID of the group order
     * @param deliveryDateTime the new delivery date and time
     */
    @Endpoint(path = "/{groupOrderId}/modify-delivery-datetime", method = ssdbrestframework.HttpMethod.PUT)
    @Response(status = 204)
    public void modifyGroupOrderDeliveryDateTime(
            @PathVariable("groupOrderId") int groupOrderId,
            @RequestParam("deliveryDateTime") LocalDateTime deliveryDateTime
    ) {
        try {
            groupOrderService.modifyGroupOrderDeliveryDateTime(groupOrderId, deliveryDateTime, LocalDateTime.now());
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Group order not found: " + groupOrderId);
        } catch (IllegalArgumentException | UnsupportedOperationException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * Place a group order
     *
     * @param groupOrderId the ID of the group order
     */
    @Endpoint(path = "/{groupOrderId}/place", method = ssdbrestframework.HttpMethod.POST)
    @Response(status = 204)
    public void placeGroupOrder(@PathVariable("groupOrderId") int groupOrderId) {
        try {
            groupOrderService.place(groupOrderId, LocalDateTime.now());
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Group order not found: " + groupOrderId);
        }
    }
}
