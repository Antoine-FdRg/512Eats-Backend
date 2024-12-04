package team.k.orderService;

import commonlibrary.dto.DishDTO;
import commonlibrary.dto.IndividualOrderDTO;
import commonlibrary.model.Dish;
import commonlibrary.model.payment.PaymentFailedException;
import lombok.RequiredArgsConstructor;
import ssdbrestframework.HttpMethod;
import ssdbrestframework.annotations.Endpoint;
import ssdbrestframework.annotations.PathVariable;
import ssdbrestframework.annotations.RequestBody;
import ssdbrestframework.annotations.RequestParam;
import ssdbrestframework.annotations.Response;
import ssdbrestframework.annotations.RestController;
import team.k.groupOrderService.GroupOrderService;

import java.time.LocalDateTime;
import java.util.List;

@RestController(path = "/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final GroupOrderService groupOrderService;

    /**
     * Create an individual order
     *
     * @param individualOrderDTO the details of the individual order
     * @return the ID of the created order
     */
    @Endpoint(path = "IndividualOrder", method = HttpMethod.POST)
    @Response(status = 201) // Created
    public int createIndividualOrder(IndividualOrderDTO individualOrderDTO) {
        try {
            LocalDateTime deliveryDateTime = LocalDateTime.parse(individualOrderDTO.deliveryDateTime());
            return orderService.createIndividualOrder(
                    individualOrderDTO.userId(),
                    individualOrderDTO.restaurantId(),
                    individualOrderDTO.deliveryLocation().id(),
                    deliveryDateTime,
                    LocalDateTime.now()
            );
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * Add a dish to an order
     *
     * @param orderId the ID of the order
     * @param dishId  the ID of the dish to add
     */
    @Endpoint(path = "add-dish", method = HttpMethod.POST)
    @Response(status = 204) // No Content
    public void addDishToOrder(@RequestBody int orderId, @RequestBody int dishId) {
        try {
            orderService.addDishToOrder(orderId, dishId);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * Pay for a sub-order
     *
     * @param registeredUserID the ID of the registered user
     * @param orderId          the ID of the sub-order to pay
     */
    @Endpoint(path = "pay", method = HttpMethod.POST)
    @Response(status = 204) // No Content
    public void paySubOrder(
            @RequestBody int registeredUserID,
            @RequestBody int orderId
    ) throws Exception {
        try {
            orderService.paySubOrder(registeredUserID, orderId, LocalDateTime.now());
        } catch (PaymentFailedException e) {
            throw new PaymentFailedException(e.getMessage());
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Get available dishes for an order
     *
     * @param orderId the ID of the order
     * @return a list of available dishes
     */
    @Endpoint(path = "available-dishes", method = HttpMethod.GET)
    @Response(status = 200) // OK
    public List<DishDTO> getAvailableDishes(
            @RequestParam("order-id") int orderId
    ) {
        try {
            List<Dish> availableDishes = orderService.getAvailableDishes(orderId);
            return availableDishes.stream().map(Dish::convertDishToDishDto).toList();
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * Create a sub-order
     *
     * @param registeredUserID the ID of the registered user
     * @param restaurantId     the ID of the restaurant
     * @param groupOrderId     the ID of the group order
     */
    @Endpoint(path = "/sub-order", method = HttpMethod.POST)
    @Response(status = 201) // Created
    public void createSuborder(
            @RequestBody int registeredUserID,
            @RequestBody int restaurantId,
            @RequestBody int groupOrderId
    ) {
        try {
            orderService.createSuborder(registeredUserID, restaurantId, groupOrderId);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
