package team.k.restaurant.discount;

import team.k.enumerations.Role;
import team.k.order.SubOrder;
import team.k.restaurant.Restaurant;

/**
 * The discount strategy for a specific role
 */
public class RoleDiscount extends DiscountStrategy {
    /**
     * The discount rate, between 0 and 1
     */
    private final double discountRate;
    /**
     * The role required to get the discount
     */
    private final Role role;

    public RoleDiscount(Restaurant restaurant, double discountRate, Role role) {
        super(restaurant);
        this.discountRate = discountRate;
        this.role = role;
    }

    @Override
    public double applyDiscount(SubOrder order) {
        if (order.getUser().getRole() == role) {
            return order.getPrice() * (1 - discountRate);
        }
        return order.getPrice();
    }
}
