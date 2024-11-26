package commonlibrary.model.order;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import commonlibrary.dto.DishDTO;
import commonlibrary.dto.IndividualOrderDTO;
import commonlibrary.model.Dish;
import commonlibrary.model.Location;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class IndividualOrder extends SubOrder {
    private Location deliveryLocation;

    public IndividualOrder(OrderBuilder orderBuilder) {
        super(orderBuilder);
    }

    public IndividualOrderDTO convertIndividualOrderToIndividualOrderDto() {
        List<DishDTO> convertedDishes = getDishes().stream()
                .map(Dish::convertDishToDishDto)
                .toList();
        return new IndividualOrderDTO(getId(), String.valueOf(getPrice()), getRestaurant().getId(), getUser().getId(), convertedDishes, getStatus().toString(), getPlacedDate().toString(), getDeliveryDate().toString(), getPayment().convertPaymentToPaymentDto(), deliveryLocation.convertLocationToLocationDto());
    }

    @Override
    public void pay(LocalDateTime now) {
        super.pay(now);
        super.place(now);
    }
}
