package team.k.controllers;

import commonlibrary.model.order.IndividualOrder;
import ssdbrestframework.HttpMethod;
import ssdbrestframework.SSDBQueryProcessingException;
import ssdbrestframework.annotations.Endpoint;
import ssdbrestframework.annotations.PathVariable;
import ssdbrestframework.annotations.RequestBody;
import ssdbrestframework.annotations.Response;
import ssdbrestframework.annotations.RestController;
import team.k.repository.IndividualOrderRepository;
import team.k.repository.RegisteredUserRepository;
import team.k.repository.RestaurantRepository;

import java.util.List;
import java.util.Objects;

@RestController(path = "/individual-orders")
public class IndividualOrderController {
    @Endpoint(path = "", method = HttpMethod.GET)
    public List<IndividualOrder> findAll() {
        return IndividualOrderRepository.getInstance().findAll();
    }

    @Endpoint(path = "/get/{id}", method = HttpMethod.GET)
    public IndividualOrder findById(@PathVariable("id") int id) throws SSDBQueryProcessingException {
        throwIfIndividualOrderIdDoesNotExist(id);
        return IndividualOrderRepository.getInstance().findById(id);
    }

    @Endpoint(path = "/create", method = HttpMethod.POST)
    @Response(status = 201, message = "Individual order created successfully")
    public void add(@RequestBody IndividualOrder individualOrder) throws SSDBQueryProcessingException {
        if (IndividualOrderRepository.getInstance().findById(individualOrder.getId()) != null) {
            throw new SSDBQueryProcessingException(409, "Individual order with ID " + individualOrder.getId() + " already exists, try updating it instead.");
        }
        throwIfRestaurantIdDoesNotExist(individualOrder.getRestaurantID());
        throwIfRegisteredIdDoesNotExist(individualOrder.getUserID());
        IndividualOrderRepository.getInstance().add(individualOrder);
    }

    @Endpoint(path = "/update", method = HttpMethod.PUT)
    @Response(status = 200, message = "Individual order updated successfully")
    public void update(@RequestBody IndividualOrder individualOrder) throws SSDBQueryProcessingException {
        throwIfIndividualOrderIdDoesNotExist(individualOrder.getId());
        throwIfRestaurantIdDoesNotExist(individualOrder.getRestaurantID());
        throwIfRegisteredIdDoesNotExist(individualOrder.getUserID());
        IndividualOrder existingIndividualOrder = IndividualOrderRepository.getInstance().findById(individualOrder.getId());
        IndividualOrderRepository.getInstance().update(individualOrder, existingIndividualOrder);
    }

    @Endpoint(path = "/delete/{id}", method = HttpMethod.DELETE)
    @Response(status = 200, message = "Individual order deleted successfully")
    public void remove(@PathVariable("id") int id) throws SSDBQueryProcessingException {
        throwIfIndividualOrderIdDoesNotExist(id);
        IndividualOrderRepository.getInstance().remove(id);
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

    private static void throwIfIndividualOrderIdDoesNotExist(int subOrderID) throws SSDBQueryProcessingException {
        if (Objects.isNull(IndividualOrderRepository.getInstance().findById(subOrderID))) {
            throw new SSDBQueryProcessingException(404, "Suborder with ID " + subOrderID + " not found.");
        }
    }
}
