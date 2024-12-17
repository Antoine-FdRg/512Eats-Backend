package commonlibrary.model.restaurant.discount;

import commonlibrary.enumerations.Role;
import commonlibrary.model.RegisteredUser;
import commonlibrary.model.order.SubOrder;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

/**
 * The discount strategy for a specific role
 */
@Entity
@DiscriminatorValue("RoleDiscount")
@NoArgsConstructor
public class RoleDiscount extends DiscountStrategy {
    /**
     * The discount rate, between 0 and 1
     */
    private double discountRate;
    /**
     * The role required to get the discount
     */
    private Role role;

    public RoleDiscount(int restaurantID, double discountRate, Role role) {
        super(restaurantID);
        this.discountRate = discountRate;
        this.role = role;
    }

    @Override
    public double applyDiscount(SubOrder order, RegisteredUser user) {
        if (user.getRole() == role) {
            return order.getPrice() * (1 - discountRate);
        }
        return order.getPrice();
    }
}
