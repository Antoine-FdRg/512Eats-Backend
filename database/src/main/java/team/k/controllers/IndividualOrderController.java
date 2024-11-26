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

import java.util.List;

@RestController(path = "/individual-orders")
public class IndividualOrderController {
    @Endpoint(path = "", method = HttpMethod.GET)
    public List<IndividualOrder> findAll() {
        return IndividualOrderRepository.getInstance().findAll();
    }

    @Endpoint(path = "/get/{id}", method = HttpMethod.GET)
    public IndividualOrder findById(@PathVariable("id") int id) throws SSDBQueryProcessingException {
        IndividualOrder individualOrder = IndividualOrderRepository.getInstance().findById(id);
        if (individualOrder == null) {
            throw new SSDBQueryProcessingException(404, "Individual order with ID " + id + " not found.");
        }
        return individualOrder;
    }

    @Endpoint(path = "/create", method = HttpMethod.POST)
    @Response(status = 201, message = "Individual order created successfully")
    public void add(@RequestBody IndividualOrder individualOrder) throws SSDBQueryProcessingException {
        if (IndividualOrderRepository.getInstance().findById(individualOrder.getId()) != null) {
            throw new SSDBQueryProcessingException(409, "Individual order with ID " + individualOrder.getId() + " already exists, try updating it instead.");
        }
        IndividualOrderRepository.getInstance().add(individualOrder);
    }

    @Endpoint(path = "/update", method = HttpMethod.PUT)
    @Response(status = 200, message = "Individual order updated successfully")
    public void update(@RequestBody IndividualOrder individualOrder) throws SSDBQueryProcessingException {
        boolean success = IndividualOrderRepository.getInstance().update(individualOrder);
        if (!success) {
            throw new SSDBQueryProcessingException(404, "Individual order with ID " + individualOrder.getId() + " not found.");
        }
    }

    @Endpoint(path = "/delete/{id}", method = HttpMethod.DELETE)
    @Response(status = 200, message = "Individual order deleted successfully")
    public void remove(@PathVariable("id") int id) throws SSDBQueryProcessingException {
        boolean success = IndividualOrderRepository.getInstance().remove(id);
        if (!success) {
            throw new SSDBQueryProcessingException(404, "Individual order with ID " + id + " not found.");
        }
    }


}
