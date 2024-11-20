package commonlibrary.model.restaurant.discount;

import commonlibrary.model.order.SubOrder;
import commonlibrary.model.restaurant.Restaurant;

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
