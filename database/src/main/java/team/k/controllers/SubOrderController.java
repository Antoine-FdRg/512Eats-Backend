package team.k.controllers;

import commonlibrary.model.Dish;
import commonlibrary.model.order.SubOrder;
import ssdbrestframework.HttpMethod;
import ssdbrestframework.SSDBQueryProcessingException;
import ssdbrestframework.annotations.Endpoint;
import ssdbrestframework.annotations.PathVariable;
import ssdbrestframework.annotations.RequestBody;
import ssdbrestframework.annotations.RequestParam;
import ssdbrestframework.annotations.Response;
import ssdbrestframework.annotations.RestController;
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
            return SubOrderRepository.getInstance().findByUserId(userId);
        }
        return SubOrderRepository.getInstance().findAll();
    }

    @Endpoint(path = "/get/{id}", method = HttpMethod.GET)
    public SubOrder findById(@PathVariable("id") int id) throws SSDBQueryProcessingException {
        SubOrderRepository.throwIfSubOrderIdDoesNotExist(id);
        return SubOrderRepository.getInstance().findById(id);
    }

    @Endpoint(path = "/create", method = HttpMethod.POST)
    @Response(status = 201, message = "Suborder created successfully")
    public void add(@RequestBody SubOrder subOrder) throws SSDBQueryProcessingException {
        if (SubOrderRepository.getInstance().findById(subOrder.getId()) != null) {
            throw new SSDBQueryProcessingException(409, "Suborder with ID " + subOrder.getId() + " already exists, try updating it instead.");
        }
        RestaurantRepository.throwIfRestaurantIdDoesNotExist(subOrder.getRestaurantID());
        RegisteredUserRepository.throwIfRegisteredIdDoesNotExist(subOrder.getUserID());
        DishRepository.throwIfDishesDoNotExist(subOrder.getDishes());
        SubOrderRepository.getInstance().add(subOrder);
    }

    @Endpoint(path = "/update", method = HttpMethod.PUT)
    @Response(status = 200, message = "Suborder updated successfully")
    public void update(@RequestBody SubOrder subOrder) throws SSDBQueryProcessingException {
        SubOrderRepository.throwIfSubOrderIdDoesNotExist(subOrder.getId());
        RestaurantRepository.throwIfRestaurantIdDoesNotExist(subOrder.getRestaurantID());
        RegisteredUserRepository.throwIfRegisteredIdDoesNotExist(subOrder.getUserID());
        DishRepository.throwIfDishesDoNotExist(subOrder.getDishes());
        SubOrder existingSuborder = SubOrderRepository.getInstance().findById(subOrder.getId());
        SubOrderRepository.getInstance().update(subOrder, existingSuborder);
    }

    @Endpoint(path = "/delete/{id}", method = HttpMethod.DELETE)
    @Response(status = 200, message = "Suborder deleted successfully")
    public void remove(@PathVariable("id") int id) throws SSDBQueryProcessingException {
        SubOrderRepository.throwIfSubOrderIdDoesNotExist(id);
        SubOrderRepository.getInstance().remove(id);
    }
}
