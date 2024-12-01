package team.k.models;

import commonlibrary.dto.databaseupdator.SubOrderUpdatorDTO;
import commonlibrary.enumerations.OrderStatus;
import commonlibrary.model.Dish;
import commonlibrary.model.order.SubOrder;
import commonlibrary.model.payment.Payment;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class PersistedSubOrder {
    private int id;
    private double price;
    private int restaurantID;
    private int userID;
    private List<Integer> dishes;
    private OrderStatus status;
    private LocalDateTime placedDate;
    private LocalDateTime deliveryDate;
    private Payment payment;

    public PersistedSubOrder(SubOrder subOrder) {
        this.id = subOrder.getId();
        this.price = subOrder.getPrice();
        this.restaurantID = subOrder.getRestaurantID();
        this.userID = subOrder.getUserID();
        this.dishes = subOrder.getDishes().stream().map(Dish::getId).toList();
        this.status = subOrder.getStatus();
        this.placedDate = subOrder.getPlacedDate();
        this.deliveryDate = subOrder.getDeliveryDate();
        this.payment = subOrder.getPayment();
    }

    public PersistedSubOrder (SubOrderUpdatorDTO subOrderUpdatorDTO) {
        this.id = subOrderUpdatorDTO.id();
        this.restaurantID = subOrderUpdatorDTO.restaurantID();
        this.userID = subOrderUpdatorDTO.userID();
        this.dishes = subOrderUpdatorDTO.dishIDs();
        this.status = OrderStatus.valueOf(subOrderUpdatorDTO.status());
        this.placedDate = subOrderUpdatorDTO.placedDate();
        this.deliveryDate = subOrderUpdatorDTO.deliveryDateTime();
        if(subOrderUpdatorDTO.payment() != null) {
            this.payment = subOrderUpdatorDTO.payment().convertPaymentDtoToPayment();
        }
    }
}
