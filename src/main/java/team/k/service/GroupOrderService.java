package team.k.service;

import lombok.RequiredArgsConstructor;
import team.k.common.Location;
import team.k.order.GroupOrder;
import team.k.repository.GroupOrderRepository;
import team.k.repository.LocationRepository;
import team.k.restaurant.Restaurant;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Objects;

@RequiredArgsConstructor
public class GroupOrderService {
    private final GroupOrderRepository groupOrderRepository;
    private final LocationRepository locationRepository;

    /**
     * Create a group order
     * @param deliveryLocationId the id of the location where the suborders will be delivered
     * @param deliveryDateTime the date and time of the delivery
     * @param now the current time
     * @return the id of the created group order to share with friends
     */
    public int createGroupOrder(int deliveryLocationId, LocalDateTime deliveryDateTime, LocalDateTime now){
        Location location = locationRepository.findLocationById(deliveryLocationId);
        if (location == null) {
            throw new NoSuchElementException("Location not found");
        }
        if (Objects.isNull(deliveryDateTime)) {
            throw new IllegalArgumentException("Delivery time cannot be null when creating an individual order");
        }
        if (Objects.isNull(now)) {
            throw new IllegalArgumentException("Current time cannot be null");
        }
        if (deliveryDateTime.isBefore(now.plusMinutes(Restaurant.ORDER_PROCESSING_TIME_MINUTES))) {
            throw new IllegalArgumentException("Delivery time cannot be in the past");
        }
        GroupOrder groupOrder = new GroupOrder.Builder()
                .withDeliveryLocation(location)
                .withDate(deliveryDateTime)
                .build();
        groupOrderRepository.add(groupOrder);
        return groupOrder.getId();
    }

    public GroupOrder findGroupOrderById(int id) {
        return groupOrderRepository.findGroupOrderById(id);
    }

}
