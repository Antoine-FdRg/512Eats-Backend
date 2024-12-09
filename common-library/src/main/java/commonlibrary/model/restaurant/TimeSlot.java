package commonlibrary.model.restaurant;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import commonlibrary.enumerations.OrderStatus;
import commonlibrary.model.order.SubOrder;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        setterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE)
@NoArgsConstructor
@Entity
@Table(name = "time_slot")
public class TimeSlot {
    @Id
    private int id;
    public static final int DURATION = 30;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<SubOrder> orders;
    private LocalDateTime start;
    private int productionCapacity; //number of cooks
    private int maxNumberOfOrders;

    private static int idCounter = 0;

    public TimeSlot(LocalDateTime start, Restaurant restaurant, int productionCapacity) {
        this.start = start;
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
