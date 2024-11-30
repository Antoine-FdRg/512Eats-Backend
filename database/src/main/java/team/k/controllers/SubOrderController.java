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
import java.util.Objects;

@RestController(path = "/sub-orders")
public class SubOrderController {
    @Endpoint(path = "", method = HttpMethod.GET)
    public List<SubOrder> findAll(@RequestParam("userId") Integer userId) {
        if (userId != null && userId != 0) {
            return SubOrderRepository.getInstance().findAll().stream().filter(subOrder -> subOrder.getUserID() == userId).toList();
        }
        return SubOrderRepository.getInstance().findAll();
    }

    @Endpoint(path = "/get/{id}", method = HttpMethod.GET)
    public SubOrder findById(@PathVariable("id") int id) throws SSDBQueryProcessingException {
        throwIfSubOrderIdDoesNotExist(id);
        return SubOrderRepository.getInstance().findById(id);
    }

    @Endpoint(path = "/create", method = HttpMethod.POST)
    @Response(status = 201, message = "Suborder created successfully")
    public void add(@RequestBody SubOrder subOrder) throws SSDBQueryProcessingException {
        if (SubOrderRepository.getInstance().findById(subOrder.getId()) != null) {
            throw new SSDBQueryProcessingException(409, "Suborder with ID " + subOrder.getId() + " already exists, try updating it instead.");
        }
        throwIfRestaurantIdDoesNotExist(subOrder.getRestaurantID());
        throwIfRegisteredIdDoesNotExist(subOrder.getUserID());
        SubOrderRepository.getInstance().add(subOrder);
    }

    @Endpoint(path = "/update", method = HttpMethod.PUT)
    @Response(status = 200, message = "Suborder updated successfully")
    public void update(@RequestBody SubOrder subOrder) throws SSDBQueryProcessingException {
        throwIfSubOrderIdDoesNotExist(subOrder.getId());
        throwIfRestaurantIdDoesNotExist(subOrder.getRestaurantID());
        throwIfRegisteredIdDoesNotExist(subOrder.getUserID());
        SubOrder existingSuborder = SubOrderRepository.getInstance().findById(subOrder.getId());
        SubOrderRepository.getInstance().update(subOrder, existingSuborder);
    }

    @Endpoint(path = "/delete/{id}", method = HttpMethod.DELETE)
    @Response(status = 200, message = "Suborder deleted successfully")
    public void remove(@PathVariable("id") int id) throws SSDBQueryProcessingException {
        throwIfSubOrderIdDoesNotExist(id);
        SubOrderRepository.getInstance().remove(id);
    }

    private static void throwIfRestaurantIdDoesNotExist(int restaurantID) throws SSDBQueryProcessingException {
        if (Objects.isNull(RestaurantRepository.getInstance().findById(restaurantID))) {
            throw new SSDBQueryProcessingException(404, "Restaurant with ID " + restaurantID + " not found.");
        }
    }

    private static void throwIfRegisteredIdDoesNotExist(int userID) throws SSDBQueryProcessingException {
        if (Objects.isNull(RegisteredUserRepository.getInstance().findById(userID))) {
            throw new SSDBQueryProcessingException(404, "User with ID " + userID + " not found.");
        }
    }

    private static void throwIfSubOrderIdDoesNotExist(int subOrderID) throws SSDBQueryProcessingException {
        if (Objects.isNull(SubOrderRepository.getInstance().findById(subOrderID))) {
            throw new SSDBQueryProcessingException(404, "Suborder with ID " + subOrderID + " not found.");
        }
    }

}
