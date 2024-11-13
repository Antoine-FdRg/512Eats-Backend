package team.k.unittests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import team.k.common.enumerations.FoodType;
import team.k.common.enumerations.OrderStatus;
import team.k.common.model.order.IndividualOrder;
import team.k.common.model.order.SubOrder;
import team.k.common.model.restaurant.Restaurant;
import team.k.common.model.restaurant.TimeSlot;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class TimeSlotTest {
    Restaurant restaurant;
    TimeSlot timeSlot;

    @BeforeEach
    void setUp() {
        restaurant = new Restaurant.Builder()
                .setName("Chez jo")
                .setOpen(LocalTime.of(10, 0))
                .setClose(LocalTime.of(22, 0))
                .setFoodTypes(List.of(FoodType.PIZZA))
                .setAverageOrderPreparationTime(5)
                .build();
        timeSlot = new TimeSlot(
                LocalDateTime.of(2025, 1, 23, 10, 0),
                restaurant,
                4
        );
    }

    @Test
    void maxNumberOfOrdersIsCorrectlyInitialized1() {
        restaurant.setAverageOrderPreparationTime(10);
        TimeSlot ts = new TimeSlot(
                LocalDateTime.of(2025, 1, 10, 10, 0),
                restaurant,
                1
        );
        assertEquals(3, ts.getMaxNumberOfOrders());
    }

    @Test
    void maxNumberOfOrdersIsCorrectlyInitialized2() {
        restaurant.setAverageOrderPreparationTime(3);
        TimeSlot ts = new TimeSlot(
                LocalDateTime.of(2025, 1, 11, 10, 0),
                restaurant,
                10
        );
        assertEquals(100, ts.getMaxNumberOfOrders());
    }

    @Test
    void maxNumberOfOrdersIsCorrectlyInitialized3() {
        restaurant.setAverageOrderPreparationTime(15);
        TimeSlot ts = new TimeSlot(
                LocalDateTime.of(2025, 1, 12, 10, 0),
                restaurant,
                5
        );
        assertEquals(10, ts.getMaxNumberOfOrders());
    }

    @Test
    void getTotalMaxPreparationTime()  {
        assertEquals(120, timeSlot.getTotalMaxPreparationTime());
    }

    @Test
    void isFullWithNoOrdersShouldReturnFalse() {
        assertTrue(timeSlot.getOrders().isEmpty());
        assertFalse(timeSlot.isFull());
    }

    @Test
    void isFullWithAsMuchOrdersAsMaxShouldReturnTrue() {
        restaurant.setAverageOrderPreparationTime(30);
        TimeSlot ts = new TimeSlot(
                LocalDateTime.of(2025, 1, 13, 10, 0),
                restaurant,
                1
        );
        ts.addOrder(Mockito.mock(SubOrder.class));
        assertEquals(1, ts.getOrders().size());
        assertEquals(1, ts.getMaxNumberOfOrders());
        assertTrue(ts.isFull());
    }

    @Test
    void isFullWithLongerPreparationTimeThanMaxPreparationTimeShouldReturnTrue() {
        restaurant.setAverageOrderPreparationTime(30);
        TimeSlot ts = new TimeSlot(
                LocalDateTime.of(2025, 1, 14, 10, 0),
                restaurant,
                2
        );
        SubOrder order = Mockito.mock(SubOrder.class);
        when(order.getPreparationTime()).thenReturn(60);
        when(order.getStatus()).thenReturn(OrderStatus.PLACED);
        ts.addOrder(order);
        assertTrue(ts.isFull());
    }

    @Test
    void isFullWithLessOrdersThanMaxAndLessPreparationTimeThanMaxPreparationTimeShouldReturnFalse() {
        restaurant.setAverageOrderPreparationTime(30);
        TimeSlot ts = new TimeSlot(
                LocalDateTime.of(2025, 1, 15, 10, 0),
                restaurant,
                2
        );
        SubOrder order = Mockito.mock(SubOrder.class);
        when(order.getPreparationTime()).thenReturn(30);
        when(order.getStatus()).thenReturn(OrderStatus.PLACED);
        ts.addOrder(order);
        assertFalse(ts.isFull());
    }

    @Test
    void getFreeProductionCapacityWithAPlacedOrder() {
        TimeSlot ts = new TimeSlot(
                LocalDateTime.of(2025, 1, 16, 10, 0),
                restaurant,
                2
        );
        SubOrder order = Mockito.mock(SubOrder.class);
        when(order.getPreparationTime()).thenReturn(30);
        when(order.getStatus()).thenReturn(OrderStatus.PLACED);
        ts.addOrder(order);
        assertEquals(30, ts.getFreeProductionCapacity());
    }

    @Test
    void getFreeProductionCapacityWithAPLACEDOrderAndACREATEDOrderShouldOnlyCountThePLACED() {
        TimeSlot ts = new TimeSlot(
                LocalDateTime.of(2025, 1, 17, 10, 0),
                restaurant,
                4
        );
        SubOrder order = Mockito.mock(SubOrder.class);
        when(order.getPreparationTime()).thenReturn(20);
        when(order.getStatus()).thenReturn(OrderStatus.PLACED);
        ts.addOrder(order);
        SubOrder order2 = Mockito.mock(SubOrder.class);
        when(order2.getPreparationTime()).thenReturn(15);
        when(order2.getStatus()).thenReturn(OrderStatus.CREATED);
        ts.addOrder(order2);
        assertEquals(100, ts.getFreeProductionCapacity());
    }

    @Test
    void getFreeProductionCapacityWithTwoPLACEDOrders() {
        TimeSlot ts = new TimeSlot(
                LocalDateTime.of(2025, 1, 18, 10, 0),
                restaurant,
                3
        );
        SubOrder order1 = Mockito.mock(SubOrder.class);
        when(order1.getPreparationTime()).thenReturn(20);
        when(order1.getStatus()).thenReturn(OrderStatus.PLACED);
        ts.addOrder(order1);
        SubOrder order2 = Mockito.mock(SubOrder.class);
        when(order2.getPreparationTime()).thenReturn(15);
        when(order2.getStatus()).thenReturn(OrderStatus.PLACED);
        ts.addOrder(order2);
        assertEquals(55, ts.getFreeProductionCapacity());
    }

    @Test
    void getNumberOfCreatedOrdersWithOnlyCREATEDOrders() {
        SubOrder order1 = Mockito.mock(SubOrder.class);
        when(order1.getStatus()).thenReturn(OrderStatus.CREATED);
        timeSlot.addOrder(order1);
        IndividualOrder order2 = Mockito.mock(IndividualOrder.class);
        when(order2.getStatus()).thenReturn(OrderStatus.CREATED);
        timeSlot.addOrder(order2);
        assertEquals(2, timeSlot.getNumberOfCreatedOrders());
    }

    @Test
    void getNumberOfCreatedOrdersWithCREATEDOrdersAndPlacedOrders() {
        SubOrder order1 = Mockito.mock(SubOrder.class);
        when(order1.getStatus()).thenReturn(OrderStatus.CREATED);
        timeSlot.addOrder(order1);
        IndividualOrder order2 = Mockito.mock(IndividualOrder.class);
        when(order2.getStatus()).thenReturn(OrderStatus.PLACED);
        timeSlot.addOrder(order2);
        IndividualOrder order3 = Mockito.mock(IndividualOrder.class);
        when(order3.getStatus()).thenReturn(OrderStatus.CREATED);
        timeSlot.addOrder(order3);
        assertEquals(2, timeSlot.getNumberOfCreatedOrders());
    }

    @Test
    void getNumberOfCreatedOrdersWithOnlyPLACEDOrders() {
        SubOrder order1 = Mockito.mock(SubOrder.class);
        when(order1.getStatus()).thenReturn(OrderStatus.PLACED);
        timeSlot.addOrder(order1);
        IndividualOrder order2 = Mockito.mock(IndividualOrder.class);
        when(order2.getStatus()).thenReturn(OrderStatus.PLACED);
        timeSlot.addOrder(order2);
        assertEquals(0, timeSlot.getNumberOfCreatedOrders());
    }
}