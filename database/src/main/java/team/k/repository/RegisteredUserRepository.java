package team.k.repository;

import ssdbrestframework.SSDBQueryProcessingException;
import team.k.models.PersistedRegisteredUser;

import java.util.Objects;

public class RegisteredUserRepository extends GenericRepository<PersistedRegisteredUser> {
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

    public PersistedRegisteredUser findById(int id) {
        return findAll().stream().filter(registeredUser -> registeredUser.getId() == id).findFirst().orElse(null);
    }

    public static void throwIfRegisteredIdDoesNotExist(int userID) throws SSDBQueryProcessingException {
        if (Objects.isNull(RegisteredUserRepository.getInstance().findById(userID))) {
            throw new SSDBQueryProcessingException(404, "User with ID " + userID + " not found.");
        }
    }
}
