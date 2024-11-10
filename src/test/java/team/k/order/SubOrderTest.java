package team.k.order;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import team.k.common.Dish;
import team.k.RegisteredUser;
import team.k.enumerations.FoodType;
import team.k.enumerations.Role;
import team.k.restaurant.Restaurant;
import team.k.enumerations.OrderStatus;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SubOrderTest {

    private SubOrder subOrder;
    private RegisteredUser user;
    private Restaurant restaurant;
    private List<Dish> dishes;

    @BeforeEach
    void setUp() {
        user = new RegisteredUser("John Doe", Role.STUDENT);
        restaurant = new Restaurant.Builder().setName("512Eats").setOpen(LocalTime.of(12, 0, 0)).setClose(LocalTime.of(15, 0, 0)).setFoodTypes(List.of(FoodType.BURGER)).setAverageOrderPreparationTime(30).build();
        dishes = new ArrayList<>();
        dishes.add(new Dish.Builder().setName("pizza").setDescription("pizaa").setPrice(12.5).setPreparationTime(15).build());
        dishes.add(new Dish.Builder().setName("Salad").setDescription("salad").setPrice(7).setPreparationTime(5).build());
        dishes.add(new Dish.Builder().setName("Pasta").setDescription("psta").setPrice(10).setPreparationTime(10).build());

        subOrder = new SubOrder(
                1,
                dishes.stream().mapToDouble(Dish::getPrice).sum(),
                null,
                restaurant,
                user,
                dishes,
                OrderStatus.CREATED,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1)
        );
    }

    @Test
    void addDishIncreasesPriceAndAddsDishTest() {
        Dish newDish = new Dish.Builder().setName("pizza").setDescription("pizaa").setPrice(8).setPreparationTime(10).build();
        boolean result = subOrder.addDish(newDish);
        assertTrue(result);
        assertEquals(4, subOrder.getDishes().size());
        assertEquals(37.5, subOrder.getPrice());
    }

    @Test
    void getCheaperDishTest() {
        Dish cheapestDish = subOrder.getCheaperDish();
        assertNotNull(cheapestDish);
        assertEquals("Salad", cheapestDish.getName());
        assertEquals(7.0, cheapestDish.getPrice());
    }

    @Test
    void getPreparationTimeTest() {
        int totalPrepTime = subOrder.getPreparationTime();
        assertEquals(30, totalPrepTime);
    }

    @Test
    void cancelOrderTest() {
        subOrder.cancel();
        assertEquals(OrderStatus.CANCELED, subOrder.getStatus());
    }

    @Test
    void placeOrderTest() {
        subOrder.place(subOrder.getPlacedDate());
        assertEquals(OrderStatus.PLACED, subOrder.getStatus());
    }

    @Test
    void payOrderTest() {
        subOrder.pay();
        assertEquals(OrderStatus.PAID, subOrder.getStatus());
    }

}
