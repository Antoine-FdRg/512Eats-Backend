package team.k.controllers;

import commonlibrary.dto.databasecreation.RegisteredUserCreatorDTO;
import commonlibrary.model.RegisteredUser;
import ssdbrestframework.HttpMethod;
import ssdbrestframework.SSDBQueryProcessingException;
import ssdbrestframework.annotations.Endpoint;
import ssdbrestframework.annotations.PathVariable;
import ssdbrestframework.annotations.RequestBody;
import ssdbrestframework.annotations.Response;
import ssdbrestframework.annotations.RestController;
import team.k.repository.RegisteredUserRepository;
import team.k.repository.SubOrderRepository;

import java.util.List;


@RestController(path = "/registered-users")
public class RegisteredUserController {

    @Endpoint(path = "", method = HttpMethod.GET)
    public List<RegisteredUser> findAll() {
        return RegisteredUserRepository.getInstance().findAll();
    }

    @Endpoint(path = "/get/{id}", method = HttpMethod.GET)
    public RegisteredUser findById(@PathVariable("id") int id) throws SSDBQueryProcessingException {
        RegisteredUserRepository.throwIfRegisteredIdDoesNotExist(id);
        return RegisteredUserRepository.getInstance().findById(id);
    }

    @Endpoint(path = "/create", method = HttpMethod.POST)
    @Response(status = 201, message = "Registered user created successfully")
    public void add(@RequestBody RegisteredUserCreatorDTO registeredUserCreatorDTO) {
        RegisteredUser registeredUser = registeredUserCreatorDTO.toRegisteredUser();
        RegisteredUserRepository.getInstance().add(registeredUser);
    }

    @Endpoint(path = "/update", method = HttpMethod.PUT)
    @Response(status = 200, message = "Registered user updated successfully")
    public void update(@RequestBody RegisteredUser registeredUser) throws SSDBQueryProcessingException {
        RegisteredUserRepository.throwIfRegisteredIdDoesNotExist(registeredUser.getId());
        SubOrderRepository.throwIfSubOrdersDoNotExist(registeredUser.getOrders());
        SubOrderRepository.throwIfSubOrderIdDoesNotExist(registeredUser.getCurrentOrder().getId());
        RegisteredUser existingRegisteredUser = RegisteredUserRepository.getInstance().findById(registeredUser.getId());
        RegisteredUserRepository.getInstance().update(registeredUser, existingRegisteredUser);
    }

    @Endpoint(path = "/delete/{id}", method = HttpMethod.DELETE)
    @Response(status = 200, message = "Registered user deleted successfully")
    public void remove(@PathVariable("id") int id) throws SSDBQueryProcessingException {
        RegisteredUserRepository.throwIfRegisteredIdDoesNotExist(id);
        RegisteredUserRepository.getInstance().remove(id);
    }
}
