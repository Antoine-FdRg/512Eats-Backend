package commonlibrary.dto;

import commonlibrary.model.RegisteredUser;
import commonlibrary.repository.RegisteredUserRepository;

import java.io.IOException;

public record UserDTO(int id, String name, String role) {

    public RegisteredUser convertUserDtoToUser() throws IOException, InterruptedException {
        RegisteredUserRepository registeredUserRepository = new RegisteredUserRepository();
        return registeredUserRepository.findById(id);

    }
}
