package team.k.restaurant.discount;

import team.k.order.SubOrder;
import team.k.restaurant.Restaurant;

public class UnconditionalDiscount extends DiscountStrategy {
    /**
     * The discount rate, between 0 and 1
     */
    private final double discountRate;

    public UnconditionalDiscount(Restaurant restaurant, double discountRate) {
        super(restaurant);
        this.discountRate = discountRate;
    }

    @Override
    public double applyDiscount(SubOrder order) {
        return order.getPrice() * (1 - discountRate);
    }
}
