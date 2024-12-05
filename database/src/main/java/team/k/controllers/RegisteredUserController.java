package team.k.controllers;

import commonlibrary.dto.databasecreation.RegisteredUserCreatorDTO;
import commonlibrary.dto.databaseupdator.RegisteredUserUpdatorDTO;
import commonlibrary.model.RegisteredUser;
import commonlibrary.model.order.SubOrder;
import ssdbrestframework.HttpMethod;
import ssdbrestframework.SSDBQueryProcessingException;
import ssdbrestframework.annotations.Endpoint;
import ssdbrestframework.annotations.PathVariable;
import ssdbrestframework.annotations.RequestBody;
import ssdbrestframework.annotations.Response;
import ssdbrestframework.annotations.RestController;
import team.k.models.PersistedRegisteredUser;
import team.k.repository.RegisteredUserRepository;
import team.k.repository.SubOrderRepository;

import java.util.List;


@RestController(path = "/registered-users")
public class RegisteredUserController {

    @Endpoint(path = "", method = HttpMethod.GET)
    public List<RegisteredUser> findAll() {
        return RegisteredUserRepository.getInstance().findAll().stream()
                .map(this::mapPersistedRegisteredUserToRegisteredUser)
                .toList();
    }

    @Endpoint(path = "/get/{id}", method = HttpMethod.GET)
    public RegisteredUser findById(@PathVariable("id") int id) throws SSDBQueryProcessingException {
        RegisteredUserRepository.throwIfRegisteredIdDoesNotExist(id);
        return mapPersistedRegisteredUserToRegisteredUser(RegisteredUserRepository.getInstance().findById(id));
    }

    @Endpoint(path = "/create", method = HttpMethod.POST)
    @Response(status = 201, message = "Registered user created successfully")
    public RegisteredUser add(@RequestBody RegisteredUserCreatorDTO registeredUserCreatorDTO) {
        RegisteredUser registeredUser = registeredUserCreatorDTO.toRegisteredUser();
        RegisteredUserRepository.getInstance().add(new PersistedRegisteredUser(registeredUser));
        return registeredUser;
    }

    @Endpoint(path = "/update", method = HttpMethod.PUT)
    @Response(status = 200, message = "Registered user updated successfully")
    public RegisteredUser update(@RequestBody RegisteredUserUpdatorDTO registeredUser) throws SSDBQueryProcessingException {
        RegisteredUserRepository.throwIfRegisteredIdDoesNotExist(registeredUser.id());
        SubOrderRepository.throwIfSubOrderIdsDoNotExist(registeredUser.orderIDs());
        if(registeredUser.currentOrderID() != null){
            SubOrderRepository.throwIfSubOrderIdDoesNotExist(registeredUser.currentOrderID());
        }
        PersistedRegisteredUser existingRegisteredUser = RegisteredUserRepository.getInstance().findById(registeredUser.id());
        PersistedRegisteredUser newRegisteredUser = new PersistedRegisteredUser(registeredUser);
        RegisteredUserRepository.getInstance().update(newRegisteredUser, existingRegisteredUser);
        return mapPersistedRegisteredUserToRegisteredUser(newRegisteredUser);
    }

    @Endpoint(path = "/delete/{id}", method = HttpMethod.DELETE)
    @Response(status = 200, message = "Registered user deleted successfully")
    public void remove(@PathVariable("id") int id) throws SSDBQueryProcessingException {
        RegisteredUserRepository.throwIfRegisteredIdDoesNotExist(id);
        RegisteredUserRepository.getInstance().remove(id);
    }

    private RegisteredUser mapPersistedRegisteredUserToRegisteredUser(PersistedRegisteredUser persistedRegisteredUser) {
        SubOrder currentOrder = null;
        if(persistedRegisteredUser.getCurrentOrderID() != null){
            currentOrder = SubOrderController.mapPersistedSubOrderToSubOrder(
                    SubOrderRepository.getInstance().findById(persistedRegisteredUser.getCurrentOrderID())
            );
        }
        return new RegisteredUser(false,
                persistedRegisteredUser.getId(),
                persistedRegisteredUser.getName(),
                persistedRegisteredUser.getRole(),
                currentOrder,
                persistedRegisteredUser.getOrderIDs().stream()
                        .map(SubOrderRepository.getInstance()::findById)
                        .map(SubOrderController::mapPersistedSubOrderToSubOrder)
                        .toList()
        );
    }
}
