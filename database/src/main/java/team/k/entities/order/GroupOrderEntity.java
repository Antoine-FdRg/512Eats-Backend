package team.k.entities.order;

import io.ebean.Model;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import team.k.entities.LocationEntity;
import team.k.entities.enumeration.OrderStatusEntity;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "group_order")
@AllArgsConstructor
public class GroupOrderEntity extends Model {
    @Id
    private int id;
    private LocalDateTime deliveryDateTime;
    private OrderStatusEntity status;

    @OneToMany
    @JoinColumn(name = "id")
    private List<SubOrderEntity> subOrders;

    @ManyToOne
    @JoinColumn(name = "delivery_location_id")
    private LocationEntity deliveryLocation;


    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GroupOrderEntity groupOrder)) {
            return false;
        }
        return id == groupOrder.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
