package commonlibrary.dto.databaseupdator;

import commonlibrary.model.RegisteredUser;
import commonlibrary.model.order.SubOrder;

import java.util.List;

public record RegisteredUserUpdatorDTO(int id, String name, String role, Integer currentOrderID, List<Integer> orderIDs) {
    public RegisteredUserUpdatorDTO(RegisteredUser registeredUser) {
        this(registeredUser.getId(),
                registeredUser.getName(),
                registeredUser.getRole().name(),
                registeredUser.getCurrentOrder() == null ? null : registeredUser.getCurrentOrder().getId(),
                registeredUser.getOrders().stream().map(SubOrder::getId).toList());
    }
}
