package team.k.order;

import commonlibrary.enumerations.FoodType;
import commonlibrary.enumerations.OrderStatus;
import commonlibrary.enumerations.Role;
import commonlibrary.model.Dish;
import commonlibrary.model.RegisteredUser;
import commonlibrary.model.order.GroupOrder;
import commonlibrary.model.order.OrderBuilder;
import commonlibrary.model.order.SubOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import commonlibrary.model.restaurant.Restaurant;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SubOrderTest {

    private SubOrder subOrder;
    private RegisteredUser user;
    private Restaurant restaurant;
    private Dish pizza;
    private List<Dish> dishes;
    private GroupOrder groupOrder;
    LocalDateTime paymentDate;

    @BeforeEach
    void setUp() {
        user = new RegisteredUser("John Doe", Role.STUDENT);
        restaurant = new Restaurant.Builder()
                .setName("512Eats")
                .setOpen(LocalTime.of(12, 0, 0))
                .setClose(LocalTime.of(15, 0, 0))
                .setFoodTypes(List.of(FoodType.BURGER))
                .setAverageOrderPreparationTime(30)
                .build();
        groupOrder = new GroupOrder.Builder()
                .build();
        paymentDate = LocalDateTime.now();
        dishes = new ArrayList<>();
        pizza = new Dish.Builder().setName("pizza").setDescription("pizaa").setPrice(12.5).setPreparationTime(15).build();
        dishes.add(pizza);
        dishes.add(new Dish.Builder().setName("Salad").setDescription("salad").setPrice(7).setPreparationTime(5).build());
        dishes.add(new Dish.Builder().setName("Pasta").setDescription("psta").setPrice(10).setPreparationTime(10).build());
        subOrder = new OrderBuilder().setPrice(dishes.stream().
                        mapToDouble(Dish::getPrice).sum()).
                setRestaurantID(restaurant.getId()).
                setUserID(user.getId()).
                setDishes(dishes).
                setDeliveryTime(LocalDateTime.now().plusHours(1)).
                setPlacedDate(LocalDateTime.now())
                .build();

        groupOrder.addSubOrder(subOrder);
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
    void removeDishDecreasesPriceAndRemovesDishTest() {
        double priceBefore = subOrder.getPrice();
        boolean result = subOrder.removeDish(pizza.getId());
        assertTrue(result);
        assertEquals(2, subOrder.getDishes().size());
        assertEquals(priceBefore-pizza.getPrice(), subOrder.getPrice());
    }

    @Test
    void removeDishNotFoundTest() {
        boolean result = subOrder.removeDish(100);
        assertFalse(result);
    }

    @Test
    void  removeDishInEmptyOrderTest() {
        subOrder = new OrderBuilder().setPrice(0).
                setRestaurantID(restaurant.getId()).
                setUserID(user.getId()).
                setDishes(new ArrayList<>()).
                setDeliveryTime(LocalDateTime.now().plusHours(1)).
                setPlacedDate(LocalDateTime.now())
                .build();
        boolean result = subOrder.removeDish(100);
        assertFalse(result);
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
        subOrder.place(paymentDate, user);
        assertEquals(OrderStatus.PLACED, subOrder.getStatus());
    }

    @Test
    void payOrderTest() {
        subOrder.pay(paymentDate, restaurant, user);
        assertEquals(OrderStatus.PAID, subOrder.getStatus());
    }

}
