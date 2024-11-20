package commonlibrary.dto;

import commonlibrary.model.RegisteredUser;
import commonlibrary.repository.RegisteredUserRepository;

public record UserDTO(int id, String name, String role) {

    public RegisteredUser convertUserDtoToUser() {
        RegisteredUserRepository registeredUserRepository = new RegisteredUserRepository();
        return registeredUserRepository.findById(id);

    }
}
