package team.k.restaurant.discount;

import team.k.order.SubOrder;
import team.k.restaurant.Restaurant;

/**
 * Represents a discount strategy where a free dish is given after a certain number of orders in the restaurant.
 */
public class FreeDishAfterXOrders extends DiscountStrategy {
    /**
     * The number of orders required in the restaurant to get a free dish
     */
    private final int nbOrdersRequired;

    public FreeDishAfterXOrders(Restaurant restaurant, int nbOrdersRequired) {
        super(restaurant);
        this.nbOrdersRequired = nbOrdersRequired;
    }

    @Override
    public double applyDiscount(SubOrder order) {
        if (getNbOrderInRestaurant(order) % nbOrdersRequired == 0) {
            return order.getPrice() - order.getCheaperDish().getPrice();
        }
        return order.getPrice();

    }

    /**
     * Get the number of orders of the user in the restaurant
     * @param order the order to check
     * @return the number of orders of the user in the restaurant
     */
    public int getNbOrderInRestaurant(SubOrder order) {
        return (int) order.getUser().getOrders().stream().filter(o -> o.getRestaurant().equals(restaurant)).count();
    }
}
