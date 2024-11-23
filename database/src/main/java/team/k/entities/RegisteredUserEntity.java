package team.k.entities;

import io.ebean.Model;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team.k.entities.enumeration.RoleEntity;
import team.k.entities.order.SubOrderEntity;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "registered_user")
@NoArgsConstructor
public class RegisteredUserEntity extends Model {
    @Id
    private int id;
    private String name;
    private RoleEntity role;
    @OneToOne
    @JoinColumn(name = "id")
    private SubOrderEntity currentOrder;

    private List<SubOrderEntity> orders;
    private static int idCounter = 0;

    public RegisteredUserEntity(String name, RoleEntity role) {
        this.id = idCounter++;
        this.name = name;
        this.role = role;
        this.orders = new ArrayList<>();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof RegisteredUserEntity user)) {
            return false;
        }
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
