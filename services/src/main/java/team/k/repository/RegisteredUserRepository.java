package team.k.repository;

import commonlibrary.model.RegisteredUser;

import java.util.ArrayList;
import java.util.List;

public class RegisteredUserRepository {

    private RegisteredUserRepository() {
    }

    private static final List<RegisteredUser> registeredUsers = new ArrayList<>();

    public static void add(RegisteredUser registeredUser) {
        registeredUsers.add(registeredUser);
    }

    public static RegisteredUser findById(int id) {
        return registeredUsers.stream().filter(registeredUser -> registeredUser.getId() == id).findFirst().orElse(null);
    }

    public static List<RegisteredUser> findAll() {
        return registeredUsers;
    }

    public static void delete(RegisteredUser registeredUser) {
        registeredUsers.remove(registeredUser);
    }
}
