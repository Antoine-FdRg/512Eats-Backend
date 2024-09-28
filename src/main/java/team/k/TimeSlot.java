package team.k;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Represents a time slot for a restaurant in the database.
 */
@Getter
@Setter
@AllArgsConstructor
public class TimeSlot {

    private Date start;

    private int productionCapacity;

    private static final int DURATION = 30;

    private int maxNumberOfOrders;

    public String toString() {
        return "TimeSlot [start=" + start + ", productionCapacity=" + productionCapacity + ", maxNumberOfOrders=" + maxNumberOfOrders + "]";
    }
}
