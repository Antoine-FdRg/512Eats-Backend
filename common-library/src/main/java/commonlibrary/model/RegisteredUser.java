package commonlibrary.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import commonlibrary.dto.UserDTO;
import commonlibrary.repository.RegisteredUserRepository;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import commonlibrary.enumerations.Role;
import commonlibrary.model.order.SubOrder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@NoArgsConstructor
public class RegisteredUser {
    private int id;
    private String name;
    private Role role;
    private SubOrder currentOrder;
    private List<SubOrder> orders;
    private static int idCounter = 0;

    /**
     * Constructor for RegisteredUser
     * @param name
     * @param role
     */
    public RegisteredUser(String name, Role role) {
        this(true, name, role);
    }

    /**
     * All args constructor for RegisteredUser
     * @param id id of the user
     * @param name name of the user
     * @param role role of the user
     * @param currentOrder current order of the user
     * @param orders list of orders of the user
     */
    public RegisteredUser(int id, String name, Role role, SubOrder currentOrder, List<SubOrder> orders) {
        this(true, id, name, role, currentOrder, orders);
    }

    /**
     * All args constructor for RegisteredUser that saves the user to the repository
     * @param persisting boolean to check if the user should be saved to the repository
     * @param id id of the user
     * @param name name of the user
     * @param role role of the user
     * @param currentOrder current order of the user
     * @param orders list of orders of the user
     */
    public RegisteredUser(boolean persisting, int id, String name, Role role, SubOrder currentOrder, List<SubOrder> orders) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.currentOrder = currentOrder;
        this.orders = orders;
        if (persisting) {
            createInDatabase();
        }
    }

    /**
     * Constructor for RegisteredUser and saves it to the repository
     * @param persisting boolean to check if the user should be saved to the repository
     * @param name name of the user
     * @param role role of the user
     */
    public RegisteredUser(boolean persisting, String name, Role role) {
        this.id = idCounter++;
        this.name = name;
        this.role = role;
        this.orders = new ArrayList<>();
        if (persisting) {
            createInDatabase();
        }
    }

    private void createInDatabase() {
        try {
            RegisteredUserRepository.add(this);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean addOrderToHistory(SubOrder order) {
        boolean orderHasBeenAdded = orders.add(order);
        try {
            RegisteredUserRepository.update(this);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return orderHasBeenAdded;
    }

    public UserDTO convertUserToUserDto() {
        return new UserDTO(id, name, role.toString());
    }


}
