package team.k.service;


import commonlibrary.dto.RestaurantDTO;
import commonlibrary.enumerations.FoodType;
import commonlibrary.model.Dish;
import commonlibrary.model.restaurant.Restaurant;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import team.k.repository.RestaurantRepository;

import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RestaurantServiceTest {

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAverageValueOfRestaurantPricesRange1() throws IOException, InterruptedException {
        Restaurant restaurant = new Restaurant.Builder().setName("512Eats").setOpen(LocalTime.of(12, 0, 0)).setClose(LocalTime.of(15, 0, 0)).setFoodTypes(List.of(FoodType.BURGER)).setAverageOrderPreparationTime(30).build();
        restaurant.addDish(new Dish.Builder().setName("pizza").setDescription("pizza").setPrice(5).setPreparationTime(15).build());
        restaurant.addDish(new Dish.Builder().setName("burger").setDescription("burger").setPrice(3).setPreparationTime(15).build());

        RestaurantDTO restaurantDto=restaurant.convertRestaurantToRestaurantDTO();

        assertEquals(1,(int) restaurantDto.averagePrice());
    }

    @Test
    void testGetAverageValueOfRestaurantPricesRange2() throws IOException, InterruptedException {
        Restaurant restaurant = new Restaurant.Builder().setName("512Eats").setOpen(LocalTime.of(12, 0, 0)).setClose(LocalTime.of(15, 0, 0)).setFoodTypes(List.of(FoodType.BURGER)).setAverageOrderPreparationTime(30).build();
        restaurant.addDish(new Dish.Builder().setName("pizza").setDescription("pizza").setPrice(15).setPreparationTime(15).build());
        restaurant.addDish(new Dish.Builder().setName("burger").setDescription("burger").setPrice(10).setPreparationTime(15).build());


        RestaurantDTO restaurantDto=restaurant.convertRestaurantToRestaurantDTO();

        assertEquals(2, (int)restaurantDto.averagePrice());
    }

    @Test
    void testGetAverageValueOfRestaurantPricesRange3() throws IOException, InterruptedException {
        Restaurant restaurant = new Restaurant.Builder().setName("512Eats").setOpen(LocalTime.of(12, 0, 0)).setClose(LocalTime.of(15, 0, 0)).setFoodTypes(List.of(FoodType.BURGER)).setAverageOrderPreparationTime(30).build();
        restaurant.addDish(new Dish.Builder().setName("pizza").setDescription("pizza").setPrice(30).setPreparationTime(15).build());
        restaurant.addDish(new Dish.Builder().setName("burger").setDescription("burger").setPrice(20).setPreparationTime(15).build());

        RestaurantDTO restaurantDto=restaurant.convertRestaurantToRestaurantDTO();

        assertEquals(3, (int)restaurantDto.averagePrice());
    }

    @Test
    void testGetAverageValueOfRestaurantPricesNoDishesRange1() throws IOException, InterruptedException {
        Restaurant restaurant = new Restaurant.Builder().setName("512Eats").setOpen(LocalTime.of(12, 0, 0)).setClose(LocalTime.of(15, 0, 0)).setFoodTypes(List.of(FoodType.BURGER)).setAverageOrderPreparationTime(30).build();


        RestaurantDTO restaurantDto=restaurant.convertRestaurantToRestaurantDTO();
        assertEquals(1,(int) restaurantDto.averagePrice());
    }

}