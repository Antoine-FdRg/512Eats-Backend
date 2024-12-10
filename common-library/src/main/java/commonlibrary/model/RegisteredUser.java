package commonlibrary.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import commonlibrary.dto.UserDTO;
import commonlibrary.enumerations.Role;
import commonlibrary.model.order.SubOrder;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@NoArgsConstructor
@Entity
@Table
public class RegisteredUser {
    @Id
    private int id;
    private String name;
    private Role role;
    @OneToOne(fetch = FetchType.EAGER)
    private SubOrder currentOrder;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
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

    public UserDTO convertUserToUserDto() {
        return new UserDTO(id, name, role.toString());
    }


}
