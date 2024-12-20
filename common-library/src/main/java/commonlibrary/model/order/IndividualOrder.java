package commonlibrary.model.order;

import commonlibrary.dto.DishCreationDTO;
import commonlibrary.dto.IndividualOrderDTO;
import commonlibrary.model.Dish;
import commonlibrary.model.Location;
import commonlibrary.model.RegisteredUser;
import commonlibrary.model.restaurant.Restaurant;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "individual_order")
public class IndividualOrder extends SubOrder {
    @ManyToOne(fetch = FetchType.EAGER)
    private Location deliveryLocation;

    public IndividualOrder(OrderBuilder orderBuilder) {
        super(orderBuilder);
    }

    public IndividualOrderDTO convertIndividualOrderToIndividualOrderDto() {
        List<DishCreationDTO> convertedDishes = getDishes().stream()
                .map(Dish::convertDishToDishDto)
                .toList();
        return new IndividualOrderDTO(getId(), String.valueOf(getPrice()), getRestaurantID(), getUserID(), convertedDishes, getStatus().toString(), getPlacedDate().toString(), getDeliveryDate().toString(), getPayment().convertPaymentToPaymentDto(), deliveryLocation.convertLocationToLocationDto());
    }

    @Override
    public void pay(LocalDateTime now, Restaurant restaurant, RegisteredUser ownerOrder) {
        super.pay(now, restaurant, ownerOrder);
        super.place(now, ownerOrder);
    }
}
