package team.k.service;

import commonlibrary.model.Location;
import commonlibrary.model.RegisteredUser;
import commonlibrary.model.order.GroupOrder;
import commonlibrary.model.order.SubOrder;
import team.k.repository.GroupOrderRepository;
import team.k.repository.LocationRepository;
import commonlibrary.model.restaurant.Restaurant;
import team.k.repository.RegisteredUserRepository;
import team.k.repository.RestaurantRepository;
import team.k.repository.SubOrderRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

public class GroupOrderService {

    private GroupOrderService() {
        throw new IllegalStateException("Service class");
    }

    /**
     * Create a group order
     * @param deliveryLocationId the id of the location where the suborders will be delivered
     * @param deliveryDateTime the date and time of the delivery
     * @param now the current time
     * @return the id of the created group order to share with friends
     */
    public static int createGroupOrder(int deliveryLocationId, LocalDateTime deliveryDateTime, LocalDateTime now){
        Location location = LocationRepository.findLocationById(deliveryLocationId);
        if (location == null) {
            throw new NoSuchElementException("Location not found");
        }
        if (Objects.isNull(now)) {
            throw new IllegalArgumentException("Current time cannot be null");
        }
        if (!Objects.isNull(deliveryDateTime) && deliveryDateTime.isBefore(now.plusMinutes(Restaurant.ORDER_PROCESSING_TIME_MINUTES))) {
            throw new IllegalArgumentException("Delivery time cannot be this early");
        }
        GroupOrder groupOrder = new GroupOrder.Builder()
                .withDeliveryLocationID(location.getId())
                .withDate(deliveryDateTime)
                .build();
        GroupOrderRepository.add(groupOrder);
        return groupOrder.getId();
    }

    public static GroupOrder findGroupOrderById(int id) {
        return GroupOrderRepository.findGroupOrderById(id);
    }

    /**
     * Allow a registered user to modify a group order delivery datetime, if it was not set
     * @param groupOrderId the id of group order to modify the delivery datetime
     * @param deliveryDateTime the delivery datetime for the group order
     * @param now the current time (to ensure that the chosen deliveryDateTime is not too early
     */
    public static void modifyGroupOrderDeliveryDateTime(int groupOrderId, LocalDateTime deliveryDateTime, LocalDateTime now){

        if (Objects.isNull(deliveryDateTime)) {
            throw new IllegalArgumentException("Delivery datetime cannot be null");
        }
        if (Objects.isNull(now)) {
            throw new IllegalArgumentException("Current time cannot be null");
        }
        if (deliveryDateTime.isBefore(now.plusMinutes(Restaurant.ORDER_PROCESSING_TIME_MINUTES))) {
            throw new IllegalArgumentException("Delivery time cannot be this early");
        }
        GroupOrder groupOrder = GroupOrderRepository.findGroupOrderById(groupOrderId);
        if (Objects.isNull(groupOrder)) {
            throw new NoSuchElementException("Group order not found");
        }
        if (!Objects.isNull(groupOrder.getDeliveryDateTime())) {
            throw new UnsupportedOperationException("The group order delivery datetime cannot be changed if it was already set");
        }
        groupOrder.setDeliveryDateTime(deliveryDateTime);
    }

    public static void place(int groupOrderId, LocalDateTime now) {
        GroupOrder groupOrder = GroupOrderRepository.findGroupOrderById(groupOrderId);
        if (Objects.isNull(groupOrder)) {
            throw new NoSuchElementException("Group order not found");
        }
        LocalDateTime deliveryDateTime = groupOrder.getDeliveryDateTime();
        if (Objects.isNull(deliveryDateTime)) {
            throw new UnsupportedOperationException("The group order delivery datetime must be set before placing the order");
        }
        if (deliveryDateTime.isBefore(now)) {
            throw new NoSuchElementException("Delivery time cannot be in the past");
        }
        List<RegisteredUser> members = groupOrder.getSubOrders().stream()
                .map(SubOrder::getUserID)
                .map(RegisteredUserRepository::findById)
                .toList();
        groupOrder.place(now, members);
        groupOrder.getSubOrders().forEach(subOrder -> {
            subOrder.setDeliveryDate(deliveryDateTime);
            placeSubOrder(subOrder.getId());
        });
    }

    private static void placeSubOrder(int subOrderId) {
        SubOrder subOrder = SubOrderRepository.findById(subOrderId);
        if (Objects.isNull(subOrder)) {
            throw new NoSuchElementException("Suborder not found");
        }
        Restaurant restaurant = RestaurantRepository.findById(subOrder.getRestaurantID());
        if (Objects.isNull(restaurant)) {
            throw new NoSuchElementException("Restaurant not found");
        }
        LocalDateTime deliveryDateTime = subOrder.getDeliveryDate();
        if (!restaurant.isAvailable(deliveryDateTime)) {
            throw new IllegalArgumentException("Restaurant is no longer available at the chosen time");
        }
        restaurant.addOrderToTimeslot(subOrder);
    }

}
