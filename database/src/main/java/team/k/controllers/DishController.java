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

    @Endpoint(path = "/{id}", method = HttpMethod.GET)
    public Dish findById(@PathVariable("id") int id) throws SSDBQueryProcessingException {
        Dish dish = DishRepository.getInstance().findById(id);
        if(dish==null){
            throw new SSDBQueryProcessingException(404, "Dish with ID " + id + " not found.");
        }
        return dish;
    }

    @Endpoint(path = "/create", method = HttpMethod.POST)
    @Response(status = 201)
    public void add(@RequestBody Dish dish) {
        DishRepository.getInstance().add(dish);
    }

    @Endpoint(path = "/update", method = HttpMethod.PUT)
    public void update(@RequestBody Dish dish) {
        DishRepository.getInstance().update(dish);
    }

    @Endpoint(path = "/delete/{id}", method = HttpMethod.DELETE)
    @Response(status = 200, message = "Dish deleted successfully")
    public void remove(@PathVariable("id") int id) {
        DishRepository.getInstance().remove(id);
    }
}
