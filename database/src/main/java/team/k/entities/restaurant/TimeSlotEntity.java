package team.k.entities.restaurant;

import io.ebean.Model;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import team.k.entities.order.SubOrderEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a time slot for a restaurant in the database.
 */
@ToString
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "time_slot")
public class TimeSlotEntity extends Model {
    @Id
    private int id;
    public static final int DURATION = 30;
    private List<SubOrderEntity> orders;
    private LocalDateTime start;
    @ManyToOne
    @JoinColumn(name = "id")
    private RestaurantEntity restaurant;
    private int productionCapacity; //number of cooks
    private int maxNumberOfOrders;

    private static int idCounter = 0;

    public TimeSlotEntity(LocalDateTime start, RestaurantEntity restaurant, int productionCapacity) {
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


}
