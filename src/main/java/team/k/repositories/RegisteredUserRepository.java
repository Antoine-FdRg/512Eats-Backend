package team.k.repositories;

import team.k.RegisteredUser;

import java.util.ArrayList;
import java.util.List;

public class RegisteredUserRepository {

    private final List<RegisteredUser> registeredUsers = new ArrayList<>();

    public void save(RegisteredUser registeredUser) {
        registeredUsers.add(registeredUser);
    }

    public RegisteredUser findById(int id) {
        return registeredUsers.stream().filter(registeredUser -> registeredUser.getId() == id).findFirst().orElse(null);
    }

    public List<RegisteredUser> findAll() {
        return registeredUsers;
    }

    public void delete(RegisteredUser registeredUser) {
        registeredUsers.remove(registeredUser);
    }

    public void clear() {
        registeredUsers.clear();
    }
}
