package team.k.controllers;

import commonlibrary.model.RegisteredUser;
import ssdbrestframework.HttpMethod;
import ssdbrestframework.SSDBQueryProcessingException;
import ssdbrestframework.annotations.Endpoint;
import ssdbrestframework.annotations.PathVariable;
import ssdbrestframework.annotations.RequestBody;
import ssdbrestframework.annotations.Response;
import ssdbrestframework.annotations.RestController;
import team.k.repository.RegisteredUserRepository;

import java.util.List;


@RestController(path = "/registered-users")
public class RegisteredUserController {

    @Endpoint(path = "", method = HttpMethod.GET)
    public List<RegisteredUser> findAll() {
        return RegisteredUserRepository.getInstance().findAll();
    }

    @Endpoint(path = "/get/{id}", method = HttpMethod.GET)
    public RegisteredUser findById(@PathVariable("id") int id) throws SSDBQueryProcessingException {
        RegisteredUser registeredUser = RegisteredUserRepository.getInstance().findById(id);
        if (registeredUser == null) {
            throw new SSDBQueryProcessingException(404, "Registered user with ID " + id + " not found.");
        }
        return registeredUser;
    }

    @Endpoint(path = "/create", method = HttpMethod.POST)
    @Response(status = 201, message = "Registered user created successfully")
    public void add(@RequestBody RegisteredUser registeredUser) throws SSDBQueryProcessingException {
        if (RegisteredUserRepository.getInstance().findById(registeredUser.getId()) != null) {
            throw new SSDBQueryProcessingException(409, "Registered user with ID " + registeredUser.getId() + " already exists, try updating it instead.");
        }
        RegisteredUserRepository.getInstance().add(registeredUser);
    }

    @Endpoint(path = "/update", method = HttpMethod.PUT)
    @Response(status = 200, message = "Registered user updated successfully")
    public void update(@RequestBody RegisteredUser registeredUser) throws SSDBQueryProcessingException {
        RegisteredUser existingRegisteredUser = RegisteredUserRepository.getInstance().findById(registeredUser.getId());
        if (existingRegisteredUser == null) {
            throw new SSDBQueryProcessingException(404, "Registered user with ID " + registeredUser.getId() + " not found, try creating it instead.");
        }
        RegisteredUserRepository.getInstance().update(registeredUser, existingRegisteredUser);
    }

    @Endpoint(path = "/delete/{id}", method = HttpMethod.DELETE)
    @Response(status = 200, message = "Registered user deleted successfully")
    public void remove(@PathVariable("id") int id) throws SSDBQueryProcessingException {
        boolean success = RegisteredUserRepository.getInstance().remove(id);
        if (!success) {
            throw new SSDBQueryProcessingException(404, "Registered user with ID " + id + " not found.");
        }
    }
}
