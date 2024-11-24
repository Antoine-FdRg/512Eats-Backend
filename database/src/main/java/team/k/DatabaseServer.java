package team.k;

import io.ebean.Database;
import io.ebean.DatabaseFactory;
import io.ebean.config.DatabaseConfig;
import ssdbrestframework.SSDBHttpServer;
import team.k.entities.LocationEntity;
import team.k.entities.RegisteredUserEntity;
import team.k.entities.enumeration.OrderStatusEntity;
import team.k.entities.enumeration.RoleEntity;
import team.k.entities.order.GroupOrderEntity;
import team.k.entities.order.IndividualOrderEntity;
import team.k.entities.order.SubOrderEntity;
import team.k.entities.restaurant.RestaurantEntity;
import team.k.entities.enumeration.FoodTypeEntity;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class DatabaseServer {
    public static Database database;
    public static void main(String[] args) {
        DatabaseConfig config = new DatabaseConfig();
        config.loadFromProperties();
        database = DatabaseFactory.create(config);
        createTestData();
        SSDBHttpServer serv = new SSDBHttpServer(8082, "team.k");
        serv.start();
    }

    private static void createTestData() {
        LocationEntity location = new LocationEntity.Builder()
                .setNumber("123")
                .setAddress("Main St")
                .setCity("Springfield")
                .build();
        GroupOrderEntity groupOrder = new GroupOrderEntity(1, LocalDateTime.now(), OrderStatusEntity.CREATED, new ArrayList<>(), location);
        groupOrder.save();
        RestaurantEntity restaurant = new RestaurantEntity.Builder()
                .setName("Pizza Hut")
                .setDescription("A pizza restaurant")
                .setClose(LocalTime.of(23, 0))
                .setOpen(LocalTime.of(10, 0))
                .setFoodTypes(List.of(FoodTypeEntity.FAST_FOOD, FoodTypeEntity.PIZZA))
                .setId(2)
                .build();
        restaurant.save();
        RegisteredUserEntity user = new RegisteredUserEntity("John", RoleEntity.STUDENT);
        user.save();
        SubOrderEntity subOrder = new SubOrderEntity(1, 10, groupOrder, restaurant, user, new ArrayList<>(), OrderStatusEntity.COMPLETED, LocalDateTime.now(), LocalDateTime.now(), null);
        subOrder.save();
        IndividualOrderEntity individualOrder = new IndividualOrderEntity(location);
        individualOrder.save();
    }
}