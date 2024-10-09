package team.k.restaurant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import team.k.enumerations.OrderStatus;
import team.k.order.SubOrder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a time slot for a restaurant in the database.
 */
@Getter
@Setter()
@AllArgsConstructor
public class TimeSlot {
    private int id;
    private List<SubOrder> orders;

    private LocalDateTime start;

    private Restaurant restaurant;

    private int productionCapacity; //number of cooks

    public static final int DURATION = 30;

    private int maxNumberOfOrders;

    private static int idCounter = 0;

    public TimeSlot(LocalDateTime start, Restaurant restaurant, int productionCapacity, int maxNumberOfOrders) {
        this.start = start;
        this.restaurant = restaurant;
        this.productionCapacity = productionCapacity;
        this.maxNumberOfOrders = maxNumberOfOrders;
        this.orders = new ArrayList<>();
        this.id = idCounter++;
    }

    public boolean isFull() {
        int totalPreparationTime = getTotalPreparationTime();
        return orders.size() >= maxNumberOfOrders && totalPreparationTime >= productionCapacity*DURATION;
    }

    public int getFreeProductionCapacity() {
        int totalPreparationTime = getTotalPreparationTime();
        return productionCapacity*DURATION - totalPreparationTime;
    }

    private int getTotalPreparationTime() {
        return orders.stream().filter(order -> order.getStatus().equals(OrderStatus.PLACED)).mapToInt(SubOrder::getPreparationTime).sum();
    }

    public String toString() {
        return "TimeSlot [start=" + start + ", productionCapacity=" + productionCapacity + ", maxNumberOfOrders=" + maxNumberOfOrders + "]";
    }
}
