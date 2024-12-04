package team.k.groupOrderService;

import commonlibrary.model.Location;
import commonlibrary.model.RegisteredUser;
import commonlibrary.model.order.GroupOrder;
import commonlibrary.model.restaurant.Restaurant;
import commonlibrary.repository.RegisteredUserRepository;
import lombok.RequiredArgsConstructor;
import commonlibrary.repository.GroupOrderRepository;
import commonlibrary.repository.LocationRepository;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@RequiredArgsConstructor
public class GroupOrderService {

    /**
     * Create a group order
     * @param deliveryLocationId the id of the location where the suborders will be delivered
     * @param deliveryDateTime the date and time of the delivery
     * @param now the current time
     * @return the id of the created group order to share with friends
     */
    public int createGroupOrder(int deliveryLocationId, LocalDateTime deliveryDateTime, LocalDateTime now) throws IOException, InterruptedException {
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

        return GroupOrderRepository.add(groupOrder).getId();
    }

    public GroupOrder findGroupOrderById(int id) throws IOException, InterruptedException {
        return GroupOrderRepository.findGroupOrderById(id);
    }

    /**
     * Allow a registered user to modify a group order delivery datetime, if it was not set
     * @param groupOrderId the id of group order to modify the delivery datetime
     * @param deliveryDateTime the delivery datetime for the group order
     * @param now the current time (to ensure that the chosen deliveryDateTime is not too early
     */
    public void modifyGroupOrderDeliveryDateTime(int groupOrderId, LocalDateTime deliveryDateTime, LocalDateTime now) throws IOException, InterruptedException {

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
            throw new UnsupportedOperationException("The group order delivery datetime cannot be changed");
        }
        groupOrder.setDeliveryDateTime(deliveryDateTime);
    }

    public void place(int groupOrderId, LocalDateTime now) throws IOException, InterruptedException {
        GroupOrder groupOrder = GroupOrderRepository.findGroupOrderById(groupOrderId);
        if (Objects.isNull(groupOrder)) {
            throw new NoSuchElementException("Group order not found");
        }
        List<RegisteredUser> members = groupOrder.getSubOrders().stream().map(subOrder -> {
            RegisteredUser orderOwner = null;
            try {
                orderOwner = RegisteredUserRepository.findById(subOrder.getUserID());
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (Objects.isNull(orderOwner)) {
                throw new NoSuchElementException("Order owner not found");
            }
            return orderOwner;
        }).toList();
        groupOrder.place(now, members);
    }

}
