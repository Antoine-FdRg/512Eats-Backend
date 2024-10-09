package team.k.service;

import lombok.Getter;
import team.k.common.Location;
import team.k.order.GroupOrder;
import team.k.repository.GroupOrderRepository;
import team.k.repository.LocationRepository;

public class OrderService {
    LocationRepository locationRepository;
    @Getter
    GroupOrderRepository groupOrderRepository = new GroupOrderRepository();

    public void createGroupOrder(int deliveryLocation) {
        Location location = locationRepository.getLocationById(deliveryLocation);
        if (location == null) {
            throw new IllegalArgumentException("Location not found");
        }
        GroupOrder groupOrder = new GroupOrder.Builder()
                .withDeliveryLocation(location)
                .build();

        groupOrderRepository.add(groupOrder);
    }
}
