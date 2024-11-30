package team.k.controllers;

import commonlibrary.model.Dish;
import ssdbrestframework.HttpMethod;
import ssdbrestframework.SSDBQueryProcessingException;
import ssdbrestframework.annotations.Endpoint;
import ssdbrestframework.annotations.PathVariable;
import ssdbrestframework.annotations.RequestBody;
import ssdbrestframework.annotations.Response;
import ssdbrestframework.annotations.RestController;
import team.k.repository.DishRepository;

import java.util.List;

@RestController(path = "/dishes")
public class DishController {

    @Endpoint(path = "", method = HttpMethod.GET)
    public List<Dish> findAll() {
        return DishRepository.getInstance().findAll();
    }

    @Endpoint(path = "/get/{id}", method = HttpMethod.GET)
    public Dish findById(@PathVariable("id") int id) throws SSDBQueryProcessingException {
        DishRepository.throwIfDishIdDoesNotExist(id);
        return DishRepository.getInstance().findById(id);
    }

    @Endpoint(path = "/create", method = HttpMethod.POST)
    @Response(status = 201, message = "Dish created successfully")
    public void add(@RequestBody Dish dish) throws SSDBQueryProcessingException {
        if(DishRepository.getInstance().findById(dish.getId()) != null){
            throw new SSDBQueryProcessingException(409, "Dish with ID " + dish.getId() + " already exists, try updating it instead.");
        }
        DishRepository.getInstance().add(dish);
    }

    @Endpoint(path = "/update", method = HttpMethod.PUT)
    @Response(status = 200, message = "Dish updated successfully")
    public void update(@RequestBody Dish dish) throws SSDBQueryProcessingException {
        DishRepository.throwIfDishIdDoesNotExist(dish.getId());
        Dish existingDish = DishRepository.getInstance().findById(dish.getId());
        DishRepository.getInstance().update(dish, existingDish);
    }

    @Endpoint(path = "/delete/{id}", method = HttpMethod.DELETE)
    @Response(status = 200, message = "Dish deleted successfully")
    public void remove(@PathVariable("id") int id) throws SSDBQueryProcessingException {
        DishRepository.throwIfDishIdDoesNotExist(id);
        DishRepository.getInstance().remove(id);
    }
}