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
import ssdbrestframework.annotations.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController(path = "/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;


    @Endpoint(path = "IndividualOrder", method = HttpMethod.POST)
    public int createIndividualOrder(IndividualOrderDTO individualOrderDTO) {
        try {
            LocalDateTime deliveryDateTime = LocalDateTime.parse(individualOrderDTO.deliveryDateTime());
            return orderService.createIndividualOrder(individualOrderDTO.userId(), individualOrderDTO.restaurantId(), individualOrderDTO.deliveryLocation().id(), deliveryDateTime, LocalDateTime.now());
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @Endpoint(path = "{orderId}/dishes/{dishId}", method = HttpMethod.POST)
    public void addDishToOrder(@PathVariable("orderId") int orderId, @PathVariable("dishId") int dishId) {
        try {
            orderService.addDishToOrder(orderId, dishId);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    //TODO: vérifier si ce n'est pas une requête patch qu'il faudrait faire car on update la commande ici
    @Endpoint(path = "{orderId}/place", method = HttpMethod.POST)
    public void placeSubOrder(@PathVariable("orderId") int orderId) {
        try {
            orderService.placeSubOrder(orderId, LocalDateTime.now());
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    //TODO: vérifier si ce n'est pas une requête patch qu'il faudrait faire car on update la commande ici
    @Endpoint(path = "{orderId}/pay", method = HttpMethod.POST)
    public void paySubOrder(
            @RequestParam("registeredUserId") int registeredUserID,
            @PathVariable("orderId") int orderId
    ) throws Exception {
        try {
            orderService.paySubOrder(registeredUserID, orderId, LocalDateTime.now());
        } catch (PaymentFailedException e) {
            throw new PaymentFailedException(e.getMessage());
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Endpoint(path = "{orderId}/available-dishes", method = HttpMethod.GET)
    public List<DishDTO> getAvailableDishes(
            @PathVariable("orderId") int orderId
    ) {
        try {
            List<Dish> availableDishes = orderService.getAvailableDishes(orderId);
            return availableDishes.stream().map(Dish::convertDishToDishDto).toList();
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    //TODO: vérifier si cette fonction a un intérêt d'être là + renvoyer l'id de la suborder si elle est créée avec succès
    @Endpoint(path = "/Suborder", method = HttpMethod.POST)
    public void createSuborder(
            @RequestBody int registeredUserID,
            @RequestParam("restaurantId") int restaurantId,
            @RequestParam("groupOrderId") int groupOrderId
    ) {
        try {
            orderService.createSuborder(registeredUserID, restaurantId, groupOrderId);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }


}
