package commonlibrary.model.order;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import commonlibrary.dto.DishDTO;
import commonlibrary.dto.IndividualOrderDTO;
import commonlibrary.model.Dish;
import commonlibrary.model.RegisteredUser;
import commonlibrary.model.restaurant.Restaurant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@NoArgsConstructor
public class IndividualOrder extends SubOrder {
    private int deliveryLocationID;

    public IndividualOrder(OrderBuilder orderBuilder) {
        super(orderBuilder);
        this.deliveryLocationID = orderBuilder.deliveryLocationID;
    }

    public IndividualOrderDTO convertIndividualOrderToIndividualOrderDto() {
        List<DishDTO> convertedDishes = getDishes().stream()
                .map(Dish::convertDishToDishDto)
                .toList();
        return new IndividualOrderDTO(getId(), String.valueOf(getPrice()), getRestaurantID(), getUserID(), convertedDishes, getStatus().toString(), getPlacedDate().toString(), getDeliveryDate().toString(), getPayment().convertPaymentToPaymentDto(), deliveryLocationID);
    }

    @Override
    public void pay(LocalDateTime now, Restaurant restaurant, RegisteredUser ownerOrder) {
        super.pay(now, restaurant,ownerOrder);
        super.place(now,ownerOrder);
    }
}
