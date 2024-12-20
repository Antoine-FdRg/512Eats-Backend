package commonlibrary.enumerations;

public enum OrderStatus {
    CREATED, // Dishes can be added or removed, can be payed
    PAID, // Dishes can't be added or removed, individual orders can never be in this state, they become PLACED directly
    PLACED, // The restaurant has received the order
    DELIVERING, // The order is on its way to the customer
    COMPLETED, // The order has been delivered
    DISCOUNT_USED, // The order has been used in a discount computation
    CANCELED // The order has been canceled
}
