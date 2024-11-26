package team.k.controllers;


import commonlibrary.model.Dish;
import commonlibrary.model.order.GroupOrder;
import ssdbrestframework.HttpMethod;
import ssdbrestframework.SSDBQueryProcessingException;
import ssdbrestframework.annotations.Endpoint;
import ssdbrestframework.annotations.PathVariable;
import ssdbrestframework.annotations.RequestBody;
import ssdbrestframework.annotations.Response;
import ssdbrestframework.annotations.RestController;
import team.k.repository.DishRepository;
import team.k.repository.GroupOrderRepository;

import java.util.List;

@RestController(path = "/group-orders")
public class GroupOrderController {

    @Endpoint(path = "", method = HttpMethod.GET)
    public List<GroupOrder> findAll() {
        return GroupOrderRepository.getInstance().findAll();
    }

    @Endpoint(path = "/get/{id}", method = HttpMethod.GET)
    public GroupOrder findById(@PathVariable("id") int id) throws SSDBQueryProcessingException {
        GroupOrder groupOrder = GroupOrderRepository.getInstance().findById(id);
        if (groupOrder == null) {
            throw new SSDBQueryProcessingException(404, "Group Order with ID " + id + " not found.");
        }
        return groupOrder;
    }

    @Endpoint(path = "/create", method = HttpMethod.POST)
    @Response(status = 201, message = "GroupOrder created successfully")
    public void add(@RequestBody GroupOrder groupOrder) throws SSDBQueryProcessingException {
        if (GroupOrderRepository.getInstance().findById(groupOrder.getId()) != null) {
            throw new SSDBQueryProcessingException(409, "Group order with ID " + groupOrder.getId() + " already exists, try updating it instead.");
        }
        GroupOrderRepository.getInstance().add(groupOrder);
    }

    @Endpoint(path = "/update", method = HttpMethod.PUT)
    @Response(status = 200, message = "Group order updated successfully")
    public void update(@RequestBody GroupOrder groupOrder) throws SSDBQueryProcessingException {
        boolean success = GroupOrderRepository.getInstance().update(groupOrder);
        if (!success) {
            throw new SSDBQueryProcessingException(404, "Group order with ID " + groupOrder.getId() + " not found.");
        }
    }

    @Endpoint(path = "/delete/{id}", method = HttpMethod.DELETE)
    @Response(status = 200, message = "Group order deleted successfully")
    public void remove(@PathVariable("id") int id) throws SSDBQueryProcessingException {
        boolean success = GroupOrderRepository.getInstance().remove(id);
        if (!success) {
            throw new SSDBQueryProcessingException(404, "Group order with ID " + id + " not found.");
        }
    }
}
