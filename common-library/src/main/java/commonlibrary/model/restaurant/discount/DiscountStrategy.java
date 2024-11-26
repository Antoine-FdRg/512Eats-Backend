package commonlibrary.model.restaurant.discount;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import commonlibrary.model.order.SubOrder;
import commonlibrary.model.restaurant.Restaurant;
import lombok.AllArgsConstructor;

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
    protected Restaurant restaurant;

    public DiscountStrategy(Restaurant restaurant) {
        this.restaurant = restaurant;
        this.id = idCounter++;
    }

    /**
     * Apply discount to the order
     *
     * @param order the order to apply discount to
     * @return the discounted price
     */
    public abstract double applyDiscount(SubOrder order);
}
