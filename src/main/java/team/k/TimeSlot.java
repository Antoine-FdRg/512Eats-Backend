package team.k;

import java.time.LocalDateTime;

/**
 * Represents a time slot for a restaurant in the database.
 */
public class TimeSlot {

    private LocalDateTime start;

    private int productionCapacity;

    private static final int DURATION=30;

    private int maxNumberOfOrders;

    public TimeSlot(LocalDateTime start, int productionCapacity, int maxNumberOfOrders) {
        this.start = start;
        this.productionCapacity = productionCapacity;
        this.maxNumberOfOrders = maxNumberOfOrders;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public int getProductionCapacity() {
        return productionCapacity;
    }

    public int getMaxNumberOfOrders() {
        return maxNumberOfOrders;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public void setProductionCapacity(int productionCapacity) {
        this.productionCapacity = productionCapacity;
    }

    public void setMaxNumberOfOrders(int maxNumberOfOrders) {
        this.maxNumberOfOrders = maxNumberOfOrders;
    }

    public static int getDuration() {
        return DURATION;
    }

    public String toString() {
        return "TimeSlot [start=" + start + ", productionCapacity=" + productionCapacity + ", maxNumberOfOrders=" + maxNumberOfOrders + "]";
    }
}
