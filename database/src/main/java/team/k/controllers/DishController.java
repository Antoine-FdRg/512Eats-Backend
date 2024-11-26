package team.k.controllers;

import commonlibrary.model.Dish;
import ssdbrestframework.HttpMethod;
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

    @Endpoint(path = "/{id}", method = HttpMethod.GET)
    public Dish findById(@PathVariable("id") int id) {
        return DishRepository.getInstance().findById(id);
    }

    @Endpoint(path = "", method = HttpMethod.POST)
    @Response(status = 201)
    public void add(@RequestBody Dish dish) {
        DishRepository.getInstance().add(dish);
    }

    @Endpoint(path = "/{id}", method = HttpMethod.PUT)
    public void update(@RequestBody Dish dish) {
        DishRepository.getInstance().update(dish);
    }

    @Endpoint(path = "/{id}", method = HttpMethod.DELETE)
    public void remove(@PathVariable("id") int id) {
        DishRepository.getInstance().remove(id);
    }
}
