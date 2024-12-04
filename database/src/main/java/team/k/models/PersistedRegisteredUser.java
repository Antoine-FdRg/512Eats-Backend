package team.k.models;

import commonlibrary.dto.databaseupdator.RegisteredUserUpdatorDTO;
import commonlibrary.enumerations.Role;
import commonlibrary.model.RegisteredUser;
import commonlibrary.model.order.SubOrder;
import lombok.Getter;

import java.util.List;

@Getter
public class PersistedRegisteredUser {
    private final int id;
    private final String name;
    private final Role role;
    private final Integer currentOrderID;
    private final List<Integer> orderIDs;

    public PersistedRegisteredUser(RegisteredUserUpdatorDTO registeredUser) {
        this.id = registeredUser.id();
        this.name = registeredUser.name();
        this.role = Role.valueOf(registeredUser.role());
        this.currentOrderID = registeredUser.currentOrderID();
        this.orderIDs = registeredUser.orderIDs();
    }

    public PersistedRegisteredUser(RegisteredUser registeredUser) {
        this.id = registeredUser.getId();
        this.name = registeredUser.getName();
        this.role = registeredUser.getRole();
        if(registeredUser.getCurrentOrder() != null){
            this.currentOrderID = registeredUser.getCurrentOrder().getId();
        }else{
            this.currentOrderID = null;
        }
        this.orderIDs = registeredUser.getOrders().stream().map(SubOrder::getId).toList();
    }
}
