package team.k.entities.order;

import io.ebean.Model;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@Entity
@Table(name = "payment")
@NoArgsConstructor
public class PaymentEntity extends Model {
    @Id
    private int id;
    private double amount;
    private LocalDateTime time;
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof PaymentEntity payment)) {
            return false;
        }
        return id == payment.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
