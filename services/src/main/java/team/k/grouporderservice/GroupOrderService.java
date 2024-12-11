package team.k.grouporderservice;

import commonlibrary.model.Location;
import commonlibrary.model.RegisteredUser;
import commonlibrary.model.order.GroupOrder;
import commonlibrary.model.order.SubOrder;
import commonlibrary.repository.GroupOrderJPARepository;
import commonlibrary.repository.LocationJPARepository;
import commonlibrary.repository.RegisteredUserJPARepository;
import commonlibrary.repository.RestaurantJPARepository;
import commonlibrary.repository.SubOrderJPARepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import commonlibrary.model.restaurant.Restaurant;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
public class GroupOrderService {
    private final LocationJPARepository locationJPARepository;
    private final GroupOrderJPARepository groupOrderJPARepository;
    private final RegisteredUserJPARepository registeredUserJPARepository;
    private final SubOrderJPARepository subOrderJPARepository;
    private final RestaurantJPARepository restaurantJPARepository;

    @Autowired
    GroupOrderService(LocationJPARepository locationJPARepository, GroupOrderJPARepository groupOrderJPARepository, RegisteredUserJPARepository registeredUserJPARepository, SubOrderJPARepository subOrderJPARepository, RestaurantJPARepository restaurantJPARepository) {
        this.locationJPARepository = locationJPARepository;
        this.groupOrderJPARepository = groupOrderJPARepository;
        this.registeredUserJPARepository = registeredUserJPARepository;
        this.subOrderJPARepository = subOrderJPARepository;
        this.restaurantJPARepository = restaurantJPARepository;
    }


    /**
     * Create a group order
     * @param deliveryLocationId the id of the location where the suborders will be delivered
     * @param deliveryDateTime the date and time of the delivery
     * @param now the current time
     * @return the id of the created group order to share with friends
     */
    @Transactional
    public int createGroupOrder(int deliveryLocationId, LocalDateTime deliveryDateTime, LocalDateTime now){
        Location location = locationJPARepository.findById((long) deliveryLocationId).orElse(null);
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
        groupOrderJPARepository.save(groupOrder);
        return groupOrder.getId();
    }

    public GroupOrder findGroupOrderById(int id) {
        return groupOrderJPARepository.findById((long)id).orElse(null);
    }

    /**
     * Allow a registered user to modify a group order delivery datetime, if it was not set
     * @param groupOrderId the id of group order to modify the delivery datetime
     * @param deliveryDateTime the delivery datetime for the group order
     * @param now the current time (to ensure that the chosen deliveryDateTime is not too early
     */
    @Transactional
    public void modifyGroupOrderDeliveryDateTime(int groupOrderId, LocalDateTime deliveryDateTime, LocalDateTime now){

        if (Objects.isNull(deliveryDateTime)) {
            throw new IllegalArgumentException("Delivery datetime cannot be null");
        }
        if (Objects.isNull(now)) {
            throw new IllegalArgumentException("Current time cannot be null");
        }
        if (deliveryDateTime.isBefore(now.plusMinutes(Restaurant.ORDER_PROCESSING_TIME_MINUTES))) {
            throw new IllegalArgumentException("Delivery time cannot be this early");
        }
        GroupOrder groupOrder = groupOrderJPARepository.findById((long) groupOrderId).orElse(null);
        if (Objects.isNull(groupOrder)) {
            throw new NoSuchElementException("Group order not found");
        }
        if (!Objects.isNull(groupOrder.getDeliveryDateTime())) {
            throw new UnsupportedOperationException("The group order delivery datetime cannot be changed if it was already set");
        }
        groupOrder.setDeliveryDateTime(deliveryDateTime);
    }

    @Transactional
    public void place(int groupOrderId, LocalDateTime now) {
        GroupOrder groupOrder = groupOrderJPARepository.findById((long) groupOrderId).orElse(null);
        if (Objects.isNull(groupOrder)) {
            throw new NoSuchElementException("Group order not found");
        }
        if(groupOrder.getSubOrders().isEmpty()){
            throw new UnsupportedOperationException("Group order must have at least one suborder to be placed");
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
                .map(id->registeredUserJPARepository.findById((long)id).orElse(null))
                .filter(Objects::nonNull)
                .toList();
        groupOrder.place(now, members);
        groupOrder.getSubOrders().forEach(subOrder -> {
            subOrder.setDeliveryDate(deliveryDateTime);
            placeSubOrder(subOrder.getId());
        });
    }

    @Transactional
    protected void placeSubOrder(int subOrderId) {
        SubOrder subOrder = subOrderJPARepository.findById((long)subOrderId).orElse(null);
        if (Objects.isNull(subOrder)) {
            throw new NoSuchElementException("Suborder not found");
        }
        Restaurant restaurant = restaurantJPARepository.findById((long)subOrder.getRestaurantID()).orElse(null);
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
