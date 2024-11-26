package commonlibrary.model.restaurant.discount;

import commonlibrary.model.order.SubOrder;

public class UnconditionalDiscount extends DiscountStrategy {
    /**
     * The discount rate, between 0 and 1
     */
    private final double discountRate;

    public UnconditionalDiscount(int restaurantID, double discountRate) {
        super(restaurantID);
        this.discountRate = discountRate;
    }

    @Override
    public double applyDiscount(SubOrder order) {
        return order.getPrice() * (1 - discountRate);
    }
}
