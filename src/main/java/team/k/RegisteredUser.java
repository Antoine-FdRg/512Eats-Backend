package team.k;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisteredUser {
    private Role role;
    private List<SubOrder> orders;
    private int id;

    public boolean addOrderToHistory(SubOrder order) {
        return orders.add(order);
    }
}
