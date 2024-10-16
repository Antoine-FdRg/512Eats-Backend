package team.k.external;

import lombok.AllArgsConstructor;
import team.k.RegisteredUser;
import team.k.order.SubOrder;

@AllArgsConstructor
public class PaymentProcessor {
    private RegisteredUser registeredUser;
    private SubOrder order;

    public boolean processPayment() {
        return true;
    }
}