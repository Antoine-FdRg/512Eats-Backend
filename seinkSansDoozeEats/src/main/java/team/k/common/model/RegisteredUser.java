package team.k.common.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import team.k.common.enumerations.Role;
import team.k.common.model.order.SubOrder;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class RegisteredUser {
    private int id;
    private String name;
    private Role role;
    private SubOrder currentOrder;
    private List<SubOrder> orders;
    private static int idCounter = 0;

    public RegisteredUser(String name, Role role) {
        this.id = idCounter++;
        this.name = name;
        this.role = role;
        this.orders = new ArrayList<>();
    }

    public boolean addOrderToHistory(SubOrder order) {
        return orders.add(order);
    }


}
