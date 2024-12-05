package commonlibrary.dto.databasecreation;

import commonlibrary.enumerations.Role;
import commonlibrary.model.RegisteredUser;

public record RegisteredUserCreatorDTO(String name, String role) {

    public RegisteredUser toRegisteredUser() {
        Role role = Role.valueOf(this.role);
        return new RegisteredUser(false, name, role);
    }
}
