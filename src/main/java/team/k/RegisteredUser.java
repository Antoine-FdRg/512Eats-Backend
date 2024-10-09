package team.k;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team.k.enumerations.Role;
import team.k.order.SubOrder;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisteredUser {
    private int id;
    private String name;
    private Role role;
    private SubOrder currentOrder;
    private List<SubOrder> orders;

    public boolean addOrderToHistory(SubOrder order) {
        return orders.add(order);
    }

}
