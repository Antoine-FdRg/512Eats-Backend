package team.k.entities.enumeration;

import io.ebean.annotation.DbEnumType;
import io.ebean.annotation.DbEnumValue;

public enum OrderStatusEntity {
    CREATED("created"), // Dishes can be added or removed, can be payed
    PAID("paid"), // Dishes can't be added or removed, individual orders can never be in this state, they become PLACED directly
    PLACED("placed"), // The restaurant has received the order
    DELIVERING("delivering"), // The order is on its way to the customer
    COMPLETED("completed"), // The order has been delivered
    DISCOUNT_USED("discount_used"), // The order has been used in a discount computation
    CANCELED("canceled"); // The order has been canceled

    private final String status;

    OrderStatusEntity(String status) {
        this.status = status;
    }

    @DbEnumValue(storage = DbEnumType.VARCHAR)
    public String getStatus() {
        return status;
    }
}
