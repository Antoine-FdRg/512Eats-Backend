package commonlibrary.model.restaurant.discount;

import commonlibrary.model.RegisteredUser;
import commonlibrary.model.order.SubOrder;

/**
 * Represents a discount strategy where a free dish is given after a certain number of orders in the restaurant.
 */
public class FreeDishAfterXOrders extends DiscountStrategy {
    /**
     * The number of orders required in the restaurant to get a free dish
     */
    private final int nbOrdersRequired;

    public FreeDishAfterXOrders(int restaurantID, int nbOrdersRequired) {
        super(restaurantID);
        this.nbOrdersRequired = nbOrdersRequired;
    }

    @Override
    public double applyDiscount(SubOrder order, RegisteredUser orderOwner) {
        if (getNbOrderInRestaurant(orderOwner) % nbOrdersRequired == 0) {
            return order.getPrice() - order.getCheaperDish().getPrice();
        }
        return order.getPrice();

    }

    /**
     * Get the number of orders of the user in the restaurant
     *
     * @param orderOwner the user who owns the order
     * @return the number of orders of the user in the restaurant
     */
    public int getNbOrderInRestaurant(RegisteredUser orderOwner) {
        return (int) orderOwner.getOrders().stream().filter(o -> o.getRestaurantID() == restaurantID).count();
    }
}
