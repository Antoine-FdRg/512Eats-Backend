package commonlibrary.model.restaurant.discount;

import commonlibrary.model.RegisteredUser;
import commonlibrary.model.order.SubOrder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * The abstract class for discount strategy
 */
@AllArgsConstructor
@NoArgsConstructor
public abstract class DiscountStrategy {

    private static int idCounter = 0;
    protected int id;
    /**
     * The restaurant that apply the discount strategy
     */
    protected int restaurantID;

    public DiscountStrategy(int restaurantID) {
        this.restaurantID = restaurantID;
        this.id = idCounter++;
    }

    /**
     * Apply discount to the order
     *
     * @param order the order to apply discount to
     * @return the discounted price
     */
    public abstract double applyDiscount(SubOrder order, RegisteredUser orderOwner);
}
