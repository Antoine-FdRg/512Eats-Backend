package team.k.entities.discount;

import io.ebean.Model;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToOne;
import lombok.NoArgsConstructor;
import team.k.entities.restaurant.RestaurantEntity;

/**
 * The abstract class for discount strategy
 */
@NoArgsConstructor
@MappedSuperclass
public abstract class DiscountStrategyEntity extends Model {

    @Id
    protected int id;
    private static int idCounter = 0;
    /**
     * The restaurant that apply the discount strategy
     */
    @OneToOne
    @JoinColumn(name = "id")
    protected RestaurantEntity restaurant;

    public DiscountStrategyEntity(RestaurantEntity restaurant) {
        this.restaurant = restaurant;
        this.id = idCounter++;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof DiscountStrategyEntity discountStrategy)) {
            return false;
        }
        return id == discountStrategy.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
