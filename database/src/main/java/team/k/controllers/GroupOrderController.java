package team.k.controllers;


import commonlibrary.model.order.GroupOrder;
import ssdbrestframework.HttpMethod;
import ssdbrestframework.SSDBQueryProcessingException;
import ssdbrestframework.annotations.Endpoint;
import ssdbrestframework.annotations.PathVariable;
import ssdbrestframework.annotations.RequestBody;
import ssdbrestframework.annotations.Response;
import ssdbrestframework.annotations.RestController;
import team.k.repository.GroupOrderRepository;
import team.k.repository.LocationRepository;
import team.k.repository.SubOrderRepository;

import java.util.List;

@RestController(path = "/group-orders")
public class GroupOrderController {

    @Endpoint(path = "", method = HttpMethod.GET)
    public List<GroupOrder> findAll() {
        return GroupOrderRepository.getInstance().findAll();
    }

    @Endpoint(path = "/get/{id}", method = HttpMethod.GET)
    public GroupOrder findById(@PathVariable("id") int id) throws SSDBQueryProcessingException {
        GroupOrderRepository.throwIfGroupOrderIdDoesNotExist(id);
        return GroupOrderRepository.getInstance().findById(id);
    }

    @Endpoint(path = "/create", method = HttpMethod.POST)
    @Response(status = 201, message = "GroupOrder created successfully")
    public void add(@RequestBody GroupOrder groupOrder) throws SSDBQueryProcessingException {
        if (GroupOrderRepository.getInstance().findById(groupOrder.getId()) != null) {
            throw new SSDBQueryProcessingException(409, "Group order with ID " + groupOrder.getId() + " already exists, try updating it instead.");
        }
        LocationRepository.throwIfLocationIdDoesNotExist(groupOrder.getDeliveryLocationID());
        SubOrderRepository.throwIfSubOrdersDoNotExist(groupOrder.getSubOrders());
        GroupOrderRepository.getInstance().add(groupOrder);
    }

    @Endpoint(path = "/update", method = HttpMethod.PUT)
    @Response(status = 200, message = "Group order updated successfully")
    public void update(@RequestBody GroupOrder groupOrder) throws SSDBQueryProcessingException {
        GroupOrderRepository.throwIfGroupOrderIdDoesNotExist(groupOrder.getId());
        LocationRepository.throwIfLocationIdDoesNotExist(groupOrder.getDeliveryLocationID());
        SubOrderRepository.throwIfSubOrdersDoNotExist(groupOrder.getSubOrders());
        GroupOrder existingGroupOrder = GroupOrderRepository.getInstance().findById(groupOrder.getId());
        GroupOrderRepository.getInstance().update(groupOrder, existingGroupOrder);
    }

    @Endpoint(path = "/delete/{id}", method = HttpMethod.DELETE)
    @Response(status = 200, message = "Group order deleted successfully")
    public void remove(@PathVariable("id") int id) throws SSDBQueryProcessingException {
        GroupOrderRepository.throwIfGroupOrderIdDoesNotExist(id);
        GroupOrderRepository.getInstance().remove(id);
    }
}
