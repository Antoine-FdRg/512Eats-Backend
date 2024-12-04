package team.k.controllers;

import commonlibrary.dto.databasecreation.SubOrderCreatorDTO;
import commonlibrary.dto.databaseupdator.SubOrderUpdatorDTO;
import commonlibrary.enumerations.OrderStatus;
import commonlibrary.model.order.OrderBuilder;
import commonlibrary.model.order.SubOrder;
import commonlibrary.model.payment.Payment;
import ssdbrestframework.HttpMethod;
import ssdbrestframework.SSDBQueryProcessingException;
import ssdbrestframework.annotations.Endpoint;
import ssdbrestframework.annotations.PathVariable;
import ssdbrestframework.annotations.RequestBody;
import ssdbrestframework.annotations.RequestParam;
import ssdbrestframework.annotations.Response;
import ssdbrestframework.annotations.RestController;
import team.k.models.PersistedSubOrder;
import team.k.repository.DishRepository;
import team.k.repository.RegisteredUserRepository;
import team.k.repository.RestaurantRepository;
import team.k.repository.SubOrderRepository;

import java.util.List;

@RestController(path = "/sub-orders")
public class SubOrderController {
    @Endpoint(path = "", method = HttpMethod.GET)
    public List<SubOrder> findAll(@RequestParam("userId") Integer userId) {
        if (userId != null && userId != 0) {
            return SubOrderRepository.getInstance().findByUserId(userId).stream()
                    .map(SubOrderController::mapPersistedSubOrderToSubOrder)
                    .toList();
        }
        return SubOrderRepository.getInstance().findAll().stream()
                .map(SubOrderController::mapPersistedSubOrderToSubOrder)
                .toList();
    }

    @Endpoint(path = "/get/{id}", method = HttpMethod.GET)
    public SubOrder findById(@PathVariable("id") int id) throws SSDBQueryProcessingException {
        SubOrderRepository.throwIfSubOrderIdDoesNotExist(id);
        return mapPersistedSubOrderToSubOrder(SubOrderRepository.getInstance().findById(id));
    }

    @Endpoint(path = "/create", method = HttpMethod.POST)
    @Response(status = 201, message = "Suborder created successfully")
    public SubOrder add(@RequestBody SubOrderCreatorDTO subOrderCreatorDTO) throws SSDBQueryProcessingException {
        RestaurantRepository.throwIfRestaurantIdDoesNotExist(subOrderCreatorDTO.restaurantId());
        RegisteredUserRepository.throwIfRegisteredIdDoesNotExist(subOrderCreatorDTO.userId());
        DishRepository.throwIfDishIdsDoNotExist(subOrderCreatorDTO.dishIDs());
        Payment payment = subOrderCreatorDTO.payment()!=null?subOrderCreatorDTO.payment().convertPaymentDtoToPayment():null;
        SubOrder subOrder = new OrderBuilder()
                .setRestaurantID(subOrderCreatorDTO.restaurantId())
                .setUserID(subOrderCreatorDTO.userId())
                .setDishes(subOrderCreatorDTO.dishIDs().stream()
                        .map(DishRepository.getInstance()::findById)
                        .toList())
                .setStatus(OrderStatus.valueOf(subOrderCreatorDTO.status()))
                .setPlacedDate(subOrderCreatorDTO.placedDate())
                .setDeliveryTime(subOrderCreatorDTO.deliveryDateTime())
                .setPayment(payment)
                .build();
        SubOrderRepository.getInstance().add(new PersistedSubOrder(subOrder));
        return subOrder;
    }

    @Endpoint(path = "/update", method = HttpMethod.PUT)
    @Response(status = 200, message = "Suborder updated successfully")
    public SubOrder update(@RequestBody SubOrderUpdatorDTO subOrderUpdatorDTO) throws SSDBQueryProcessingException {
        SubOrderRepository.throwIfSubOrderIdDoesNotExist(subOrderUpdatorDTO.id());
        RestaurantRepository.throwIfRestaurantIdDoesNotExist(subOrderUpdatorDTO.restaurantID());
        RegisteredUserRepository.throwIfRegisteredIdDoesNotExist(subOrderUpdatorDTO.userID());
        DishRepository.throwIfDishIdsDoNotExist(subOrderUpdatorDTO.dishIDs());
        PersistedSubOrder existingSuborder = SubOrderRepository.getInstance().findById(subOrderUpdatorDTO.id());
        PersistedSubOrder newSubOrder = new PersistedSubOrder(subOrderUpdatorDTO);
        SubOrderRepository.getInstance().update(newSubOrder,existingSuborder);
        return mapPersistedSubOrderToSubOrder(newSubOrder);
    }

    @Endpoint(path = "/delete/{id}", method = HttpMethod.DELETE)
    @Response(status = 200, message = "Suborder deleted successfully")
    public void remove(@PathVariable("id") int id) throws SSDBQueryProcessingException {
        SubOrderRepository.throwIfSubOrderIdDoesNotExist(id);
        SubOrderRepository.getInstance().remove(id);
    }

    public static SubOrder mapPersistedSubOrderToSubOrder(PersistedSubOrder persistedSubOrder) {
        return new OrderBuilder()
                .setId(persistedSubOrder.getId())
                .setRestaurantID(persistedSubOrder.getRestaurantID())
                .setUserID(persistedSubOrder.getUserID())
                .setDishes(persistedSubOrder.getDishes().stream()
                        .map(DishRepository.getInstance()::findById)
                        .toList())
                .setStatus(persistedSubOrder.getStatus())
                .setPlacedDate(persistedSubOrder.getPlacedDate())
                .setDeliveryTime(persistedSubOrder.getDeliveryDate())
                .setPayment(persistedSubOrder.getPayment())
                .build();
    }
}
