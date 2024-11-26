package team.k.repository;

import commonlibrary.model.RegisteredUser;

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
}
