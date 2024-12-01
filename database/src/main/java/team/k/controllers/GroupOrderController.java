package team.k.controllers;


import commonlibrary.dto.databasecreation.GroupOrderCreatorDTO;
import commonlibrary.dto.databaseupdator.GroupOrderUpdatorDTO;
import commonlibrary.model.order.GroupOrder;
import ssdbrestframework.HttpMethod;
import ssdbrestframework.SSDBQueryProcessingException;
import ssdbrestframework.annotations.Endpoint;
import ssdbrestframework.annotations.PathVariable;
import ssdbrestframework.annotations.RequestBody;
import ssdbrestframework.annotations.Response;
import ssdbrestframework.annotations.RestController;
import team.k.models.PersistedGroupOrder;
import team.k.repository.GroupOrderRepository;
import team.k.repository.LocationRepository;
import team.k.repository.SubOrderRepository;

import java.util.List;

@RestController(path = "/group-orders")
public class GroupOrderController {

    @Endpoint(path = "", method = HttpMethod.GET)
    public List<GroupOrder> findAll() {
        return GroupOrderRepository.getInstance().findAll().stream().map(GroupOrderController::mapPersistedGroupOrderToGroupOrder).toList();
    }

    @Endpoint(path = "/get/{id}", method = HttpMethod.GET)
    public GroupOrder findById(@PathVariable("id") int id) throws SSDBQueryProcessingException {
        GroupOrderRepository.throwIfGroupOrderIdDoesNotExist(id);
        return mapPersistedGroupOrderToGroupOrder(GroupOrderRepository.getInstance().findById(id));
    }

    @Endpoint(path = "/create", method = HttpMethod.POST)
    @Response(status = 201, message = "GroupOrder created successfully")
    public GroupOrder add(@RequestBody GroupOrderCreatorDTO groupOrderCreatorDTO) throws SSDBQueryProcessingException {
        SubOrderRepository.throwIfSubOrderIdsDoNotExist(groupOrderCreatorDTO.subOrderIDs());
        LocationRepository.throwIfLocationIdDoesNotExist(groupOrderCreatorDTO.deliveryLocationID());
        GroupOrder groupOrder = new GroupOrder.Builder()
                .withDate(groupOrderCreatorDTO.deliveryDateTime())
                .withStatus(groupOrderCreatorDTO.status())
                .withDeliveryLocationID(groupOrderCreatorDTO.deliveryLocationID())
                .withSubOrders(groupOrderCreatorDTO.subOrderIDs().stream()
                        .map(SubOrderRepository.getInstance()::findById)
                        .map(SubOrderController::mapPersistedSubOrderToSubOrder)
                        .toList())
                .build();
        GroupOrderRepository.getInstance().add(new PersistedGroupOrder(groupOrder));
        return groupOrder;
    }

    @Endpoint(path = "/update", method = HttpMethod.PUT)
    @Response(status = 200, message = "Group order updated successfully")
    public GroupOrder update(@RequestBody GroupOrderUpdatorDTO groupOrderUpdatorDTO) throws SSDBQueryProcessingException {
        GroupOrderRepository.throwIfGroupOrderIdDoesNotExist(groupOrderUpdatorDTO.id());
        SubOrderRepository.throwIfSubOrderIdsDoNotExist(groupOrderUpdatorDTO.subOrderIDs());
        LocationRepository.throwIfLocationIdDoesNotExist(groupOrderUpdatorDTO.deliveryLocationID());
        PersistedGroupOrder existingGroupOrder = GroupOrderRepository.getInstance().findById(groupOrderUpdatorDTO.id());
        GroupOrderRepository.getInstance().update(new PersistedGroupOrder(groupOrderUpdatorDTO),existingGroupOrder);
        return mapPersistedGroupOrderToGroupOrder(GroupOrderRepository.getInstance().findById(groupOrderUpdatorDTO.id()));
    }

    @Endpoint(path = "/delete/{id}", method = HttpMethod.DELETE)
    @Response(status = 200, message = "Group order deleted successfully")
    public void remove(@PathVariable("id") int id) throws SSDBQueryProcessingException {
        GroupOrderRepository.throwIfGroupOrderIdDoesNotExist(id);
        GroupOrderRepository.getInstance().remove(id);
    }

    private static GroupOrder mapPersistedGroupOrderToGroupOrder(PersistedGroupOrder persistedGroupOrder) {
        return new GroupOrder.Builder()
                .withId(persistedGroupOrder.getId())
                .withDate(persistedGroupOrder.getDeliveryDateTime())
                .withStatus(persistedGroupOrder.getStatus())
                .withSubOrders(persistedGroupOrder.getSuborderIDs().stream()
                        .map(SubOrderRepository.getInstance()::findById)
                        .map(SubOrderController::mapPersistedSubOrderToSubOrder)
                        .toList())
                .withDeliveryLocationID(persistedGroupOrder.getDeliveryLocationID())
                .build();
    }
}
