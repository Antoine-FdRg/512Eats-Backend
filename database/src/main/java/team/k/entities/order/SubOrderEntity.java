package team.k.entities.order;

import io.ebean.Model;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team.k.entities.restaurant.DishEntity;
import team.k.entities.RegisteredUserEntity;
import team.k.entities.restaurant.RestaurantEntity;
import team.k.entities.enumeration.OrderStatusEntity;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "sub_order")
@NoArgsConstructor
public class SubOrderEntity extends Model {
    @Id
    private int id;
    private double price;
    @ManyToOne
    @JoinColumn(name = "group_order_id")
    private GroupOrderEntity groupOrder;
    @ManyToOne
    private RestaurantEntity restaurant;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private RegisteredUserEntity user;
    private List<DishEntity> dishes;
    private OrderStatusEntity status;
    private LocalDateTime placedDate;
    private LocalDateTime deliveryDate;
    @OneToOne
    @JoinColumn(name = "payment_id")
    private PaymentEntity payment;


    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SubOrderEntity subOrder)) {
            return false;
        }
        return id == subOrder.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

}
