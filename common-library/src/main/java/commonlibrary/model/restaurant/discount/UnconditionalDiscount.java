package commonlibrary.model.restaurant.discount;

import commonlibrary.model.RegisteredUser;
import commonlibrary.model.order.SubOrder;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("UnconditionalDiscount")
@NoArgsConstructor
public class UnconditionalDiscount extends DiscountStrategy {
    /**
     * The discount rate, between 0 and 1
     */
    private double discountRate;

    public UnconditionalDiscount(int restaurantID, double discountRate) {
        super(restaurantID);
        this.discountRate = discountRate;
    }

    @Override
    public double applyDiscount(SubOrder order, RegisteredUser orderOwner) {
        return order.getPrice() * (1 - discountRate);
    }
}
