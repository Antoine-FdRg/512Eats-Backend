package team.k.common.model.restaurant.discount;

import lombok.AllArgsConstructor;
import team.k.common.model.restaurant.Restaurant;
import team.k.common.model.order.SubOrder;

/**
 * The abstract class for discount strategy
 */
@AllArgsConstructor
public abstract class DiscountStrategy {

    /**
     * The restaurant that apply the discount strategy
     */
    protected Restaurant restaurant;

    /**
     * Apply discount to the order
     * @param order the order to apply discount to
     * @return the discounted price
     */
    public abstract double applyDiscount(SubOrder order);
}
