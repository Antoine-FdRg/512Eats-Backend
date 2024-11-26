package team.k.controllers;

import commonlibrary.model.Dish;
import ssdbrestframework.HttpMethod;
import ssdbrestframework.annotations.Endpoint;
import ssdbrestframework.annotations.RestController;
import team.k.repository.DishRepository;

import java.util.List;

@RestController(path = "/dishes")
public class DishController {

    public DishController() {
        DishRepository.getInstance().add(
                new Dish.Builder()
                        .setName("Pizza")
                        .setDescription("A delicious pizza")
                        .setPrice(10.0)
                        .setPreparationTime(10)
                        .setPicture("pizza.jpg")
                        .build()
        );
    }

    @Endpoint(path = "", method = HttpMethod.GET)
    public List<Dish> findAll() {
        return DishRepository.getInstance().findAll();
    }
}
