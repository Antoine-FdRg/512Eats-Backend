package team.k.entities.discount;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;
import team.k.entities.enumeration.RoleEntity;
import team.k.entities.restaurant.RestaurantEntity;

/**
 * The discount strategy for a specific role
 */
@NoArgsConstructor
@Entity
@DiscriminatorValue("role")
public class RoleDiscountEntity extends DiscountStrategyEntity {
    /**
     * The discount rate, between 0 and 1
     */
    private double discountRate;
    /**
     * The role required to get the discount
     */
    private RoleEntity role;

    public RoleDiscountEntity(RestaurantEntity restaurant, double discountRate, RoleEntity role) {
        super(restaurant);
        this.discountRate = discountRate;
        this.role = role;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof RoleDiscountEntity discountStrategy)) {
            return false;
        }
        return id == discountStrategy.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
