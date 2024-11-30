package team.k.repository;

import commonlibrary.model.RegisteredUser;
import ssdbrestframework.SSDBQueryProcessingException;

import java.util.Objects;

public class RegisteredUserRepository extends GenericRepository<RegisteredUser> {
    private static RegisteredUserRepository instance;

    private RegisteredUserRepository() {
        super();
    }

    public static RegisteredUserRepository getInstance() {
        if (instance == null) {
            instance = new RegisteredUserRepository();
        }
        return instance;
    }

    public RegisteredUser findById(int id) {
        return findAll().stream().filter(registeredUser -> registeredUser.getId() == id).findFirst().orElse(null);
    }

    public static void throwIfRegisteredIdDoesNotExist(int userID) throws SSDBQueryProcessingException {
        if (Objects.isNull(RegisteredUserRepository.getInstance().findById(userID))) {
            throw new SSDBQueryProcessingException(404, "User with ID " + userID + " not found.");
        }
    }
}
