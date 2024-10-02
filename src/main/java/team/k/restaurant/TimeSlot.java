package team.k.restaurant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import team.k.order.SubOrder;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * Represents a time slot for a restaurant in the database.
 */
@Getter
@Setter
@AllArgsConstructor
public class TimeSlot {
    private List<SubOrder> orders;

    private LocalDateTime start;

    private int productionCapacity;

    public static final int DURATION = 30;

    private int maxNumberOfOrders;

    public String toString() {
        return "TimeSlot [start=" + start + ", productionCapacity=" + productionCapacity + ", maxNumberOfOrders=" + maxNumberOfOrders + "]";
    }
}
