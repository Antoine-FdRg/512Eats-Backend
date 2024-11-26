package team.k.controllers;

import commonlibrary.model.restaurant.Restaurant;
import ssdbrestframework.HttpMethod;
import ssdbrestframework.annotations.Endpoint;
import ssdbrestframework.annotations.RestController;
import team.k.repository.RestaurantRepository;

import java.util.List;

@RestController(path = "/restaurants")
public class RestaurantController {

    @Endpoint(path = "", method = HttpMethod.GET)
    public List<Restaurant> findAll() {
        return RestaurantRepository.getInstance().findAll();
    }
}
