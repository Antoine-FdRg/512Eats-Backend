package team.k.controller;

import commonlibrary.dto.SubOrderDTO;
import commonlibrary.external.PaymentProcessor;
import lombok.NoArgsConstructor;
import ssdbrestframework.HttpMethod;
import ssdbrestframework.SSDBQueryProcessingException;
import ssdbrestframework.annotations.*;
import team.k.service.OrderService;

import commonlibrary.dto.DishDTO;
import commonlibrary.dto.IndividualOrderDTO;
import commonlibrary.model.Dish;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@RestController(path = "/orders")
@NoArgsConstructor
public class OrderController {


    /**
     * Create an individual order
     *
     * @param individualOrderDTO the details of the individual order
     * @return the ID of the created order
     */
    @Endpoint(path = "/individual-order", method = HttpMethod.POST)
    @Response(status = 201) // Created
    public int createIndividualOrder(@RequestBody IndividualOrderDTO individualOrderDTO) {
        try {
            LocalDateTime deliveryDateTime = LocalDateTime.parse(individualOrderDTO.deliveryDateTime());
            return OrderService.createIndividualOrder(individualOrderDTO.userId(), individualOrderDTO.restaurantId(), individualOrderDTO.deliveryLocation().id(), deliveryDateTime, LocalDateTime.now());
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }


    public record AddDishToOrderRequest(int orderId, int dishId) {
    }

    /**
     * Add a dish to an order
     *
     * @param request the details of the dish to add
     */
    @Endpoint(path = "/add-dish", method = HttpMethod.POST)
    @Response(status = 204) // No Content
    public void addDishToOrder(@RequestBody AddDishToOrderRequest request) {
        try {
            OrderService.addDishToOrder(request.orderId, request.dishId);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public record PaySubOrderRequest(int registeredUserID, int orderId) {
    }

    /**
     * Pay for a sub-order
     *
     * @param request the details of the sub-order to pay for
     */
    @Endpoint(path = "/pay", method = HttpMethod.POST)
    @Response(status = 204) // No Content
    public void paySubOrder(@RequestBody PaySubOrderRequest request) throws SSDBQueryProcessingException {
        try {
            PaymentProcessor paymentProcessor = new PaymentProcessor();
            OrderService.paySubOrder(request.registeredUserID, request.orderId, LocalDateTime.now(), paymentProcessor);
        } catch (IllegalArgumentException e) {
            throw new SSDBQueryProcessingException(400, e.getMessage());
        }
    }

    /**
     * Get available dishes for an order
     *
     * @param orderId the ID of the order
     * @return a list of available dishes
     */
    @Endpoint(path = "/available-dishes", method = HttpMethod.GET)
    @Response(status = 200) // OK
    public List<DishDTO> getAvailableDishes(@RequestParam("order-id") int orderId) throws SSDBQueryProcessingException {
        try {
            List<Dish> availableDishes = OrderService.getAvailableDishes(orderId);
            return availableDishes.stream().map(Dish::convertDishToDishDto).toList();
        } catch (NoSuchElementException e) {
            throw new SSDBQueryProcessingException(404, e.getMessage());
        }
    }

    public record CreateSuborderRequest(int registeredUserID, int restaurantId, int groupOrderId) {
    }

    /**
     * Create a sub-order
     *
     * @param request the details of the sub-order to create
     */
    @Endpoint(path = "/sub-order", method = HttpMethod.POST)
    @Response(status = 201) // Created
    public int createSuborder(@RequestBody CreateSuborderRequest request) throws SSDBQueryProcessingException {
        try {
            return OrderService.createSuborder(request.registeredUserID, request.restaurantId, request.groupOrderId);
        } catch (NoSuchElementException e) {
            throw new SSDBQueryProcessingException(404, e.getMessage());
        }
    }

    @Endpoint(path = "/get/sub-order", method = HttpMethod.GET)
    @Response(status = 200)
    public SubOrderDTO getSubOrder(@RequestParam("order-id") int orderId) throws SSDBQueryProcessingException {
        try {
            return OrderService.getSubOrder(orderId).convertSubOrderToSubOrderDto();
        } catch (NoSuchElementException e) {
            throw new SSDBQueryProcessingException(404, e.getMessage());
        }
    }
}
