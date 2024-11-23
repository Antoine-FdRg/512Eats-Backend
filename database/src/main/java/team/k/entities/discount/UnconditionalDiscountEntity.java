package team.k.entities.discount;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;
import team.k.entities.restaurant.RestaurantEntity;

@NoArgsConstructor
@DiscriminatorValue("unconditional")
@Entity
public class UnconditionalDiscountEntity extends DiscountStrategyEntity {
    /**
     * The discount rate, between 0 and 1
     */
    private double discountRate;

    public UnconditionalDiscountEntity(RestaurantEntity restaurant, double discountRate) {
        super(restaurant);
        this.discountRate = discountRate;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof UnconditionalDiscountEntity discountStrategy)) {
            return false;
        }
        return id == discountStrategy.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
