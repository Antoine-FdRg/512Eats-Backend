package team.k.common.model.restaurant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import team.k.common.enumerations.OrderStatus;
import team.k.common.model.order.SubOrder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a time slot for a restaurant in the database.
 */
@ToString
@Getter
@Setter
@AllArgsConstructor
public class TimeSlot {
    private int id;
    public static final int DURATION = 30;
    private List<SubOrder> orders;
    private LocalDateTime start;
    private Restaurant restaurant;
    private int productionCapacity; //number of cooks
    private int maxNumberOfOrders;

    private static int idCounter = 0;

    public TimeSlot(LocalDateTime start, Restaurant restaurant, int productionCapacity) {
        this.start = start;
        this.restaurant = restaurant;
        this.productionCapacity = productionCapacity;
        this.maxNumberOfOrders = getTotalMaxPreparationTime() / restaurant.getAverageOrderPreparationTime();
        this.orders = new ArrayList<>();
        this.id = idCounter++;
    }

    public int getTotalMaxPreparationTime() {
        return productionCapacity * DURATION;
    }

    public boolean isFull() {
        return orders.size() >= maxNumberOfOrders || getTotalPreparationTime() >= getTotalMaxPreparationTime();
    }

    public int getFreeProductionCapacity() {
        return getTotalMaxPreparationTime() - getTotalPreparationTime();
    }

    private int getTotalPreparationTime() {
        return orders.stream().filter(order -> order.getStatus().equals(OrderStatus.PLACED)).mapToInt(SubOrder::getPreparationTime).sum();
    }

    private int getNumberOfPlacedOrders() {
        return orders.stream().filter(subOrder -> OrderStatus.PLACED.equals(subOrder.getStatus())).toArray().length;
    }

    public int getNumberOfCreatedOrders() {
        return orders.stream().filter(subOrder -> OrderStatus.CREATED.equals(subOrder.getStatus())).toArray().length;
    }

    public LocalDateTime getEnd() {
        return start.plusMinutes(DURATION);
    }

    public void addOrder(SubOrder order) {
        orders.add(order);
    }

}
