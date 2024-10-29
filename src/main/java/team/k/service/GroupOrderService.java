package team.k.service;

import lombok.RequiredArgsConstructor;
import team.k.common.Location;
import team.k.order.GroupOrder;
import team.k.repository.GroupOrderRepository;
import team.k.repository.LocationRepository;

@RequiredArgsConstructor
public class GroupOrderService {
    private final GroupOrderRepository groupOrderRepository;
    private final LocationRepository locationRepository;

    /**
     * Create a group order
     * @param deliveryLocation the location where the suborders will be delivered
     * @return the id of the created group order to share with friends
     */
    public int createGroupOrder(int deliveryLocation) {
        Location location = locationRepository.findLocationById(deliveryLocation);
        if (location == null) {
            throw new IllegalArgumentException("Location not found");
        }
        GroupOrder groupOrder = new GroupOrder.Builder()
                .withDeliveryLocation(location)
                .build();
        groupOrderRepository.add(groupOrder);
        return groupOrder.getId();
    }

    public GroupOrder findGroupOrderById(int id) {
        return groupOrderRepository.findGroupOrderById(id);
    }

}
