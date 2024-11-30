package team.k.controllers;

import commonlibrary.model.order.IndividualOrder;
import ssdbrestframework.HttpMethod;
import ssdbrestframework.SSDBQueryProcessingException;
import ssdbrestframework.annotations.Endpoint;
import ssdbrestframework.annotations.PathVariable;
import ssdbrestframework.annotations.RequestBody;
import ssdbrestframework.annotations.Response;
import ssdbrestframework.annotations.RestController;
import team.k.repository.DishRepository;
import team.k.repository.IndividualOrderRepository;
import team.k.repository.LocationRepository;
import team.k.repository.RegisteredUserRepository;
import team.k.repository.RestaurantRepository;

import java.util.List;

@RestController(path = "/individual-orders")
public class IndividualOrderController {
    @Endpoint(path = "", method = HttpMethod.GET)
    public List<IndividualOrder> findAll() {
        return IndividualOrderRepository.getInstance().findAll();
    }

    @Endpoint(path = "/get/{id}", method = HttpMethod.GET)
    public IndividualOrder findById(@PathVariable("id") int id) throws SSDBQueryProcessingException {
        IndividualOrderRepository.throwIfIndividualOrderIdDoesNotExist(id);
        return IndividualOrderRepository.getInstance().findById(id);
    }

    @Endpoint(path = "/create", method = HttpMethod.POST)
    @Response(status = 201, message = "Individual order created successfully")
    public void add(@RequestBody IndividualOrder individualOrder) throws SSDBQueryProcessingException {
        if (IndividualOrderRepository.getInstance().findById(individualOrder.getId()) != null) {
            throw new SSDBQueryProcessingException(409, "Individual order with ID " + individualOrder.getId() + " already exists, try updating it instead.");
        }
        LocationRepository.throwIfLocationIdDoesNotExist(individualOrder.getDeliveryLocationID());
        RestaurantRepository.throwIfRestaurantIdDoesNotExist(individualOrder.getRestaurantID());
        RegisteredUserRepository.throwIfRegisteredIdDoesNotExist(individualOrder.getUserID());
        DishRepository.throwIfDishesDoNotExist(individualOrder.getDishes());
        IndividualOrderRepository.getInstance().add(individualOrder);
    }

    @Endpoint(path = "/update", method = HttpMethod.PUT)
    @Response(status = 200, message = "Individual order updated successfully")
    public void update(@RequestBody IndividualOrder individualOrder) throws SSDBQueryProcessingException {
        IndividualOrderRepository.throwIfIndividualOrderIdDoesNotExist(individualOrder.getId());
        LocationRepository.throwIfLocationIdDoesNotExist(individualOrder.getDeliveryLocationID());
        RestaurantRepository.throwIfRestaurantIdDoesNotExist(individualOrder.getRestaurantID());
        RegisteredUserRepository.throwIfRegisteredIdDoesNotExist(individualOrder.getUserID());
        DishRepository.throwIfDishesDoNotExist(individualOrder.getDishes());
        IndividualOrder existingIndividualOrder = IndividualOrderRepository.getInstance().findById(individualOrder.getId());
        IndividualOrderRepository.getInstance().update(individualOrder, existingIndividualOrder);
    }

    @Endpoint(path = "/delete/{id}", method = HttpMethod.DELETE)
    @Response(status = 200, message = "Individual order deleted successfully")
    public void remove(@PathVariable("id") int id) throws SSDBQueryProcessingException {
        IndividualOrderRepository.throwIfIndividualOrderIdDoesNotExist(id);
        IndividualOrderRepository.getInstance().remove(id);
    }
}
