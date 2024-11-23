package team.k;

import io.ebean.Database;
import io.ebean.DatabaseFactory;
import io.ebean.config.DatabaseConfig;
import ssdbrestframework.SSDBHttpServer;
import team.k.entities.DishEntity;
import team.k.entities.RestaurantEntity;
import team.k.entities.enumeration.FoodTypeEntity;

import java.time.LocalTime;
import java.util.List;

public class DatabaseServer {
    public static void main(String[] args) {
        DatabaseConfig config = new DatabaseConfig();
        config.loadFromProperties();
        Database database = DatabaseFactory.create(config);
        createTestData(database);
        SSDBHttpServer serv = new SSDBHttpServer(8082, "team.k");
        serv.start();
    }

    private static void createTestData(Database database) {
        DishEntity dish = new DishEntity.Builder()
                .setName("pizza")
                .setDescription("A delicious pizzaaaa")
                .setPrice(10.0)
                .setId(2)
                .build();
        RestaurantEntity restaurant = new RestaurantEntity.Builder()
                .setName("Pizza Hut")
                .setDescription("A pizza restaurant")
                .setClose(LocalTime.of(23, 0))
                .setOpen(LocalTime.of(10, 0))
                .setFoodTypes(List.of(FoodTypeEntity.FAST_FOOD, FoodTypeEntity.PIZZA))
                .setId(2)
                .build();
        restaurant.addDish(dish);
        database.save(restaurant);
        System.out.println(restaurant);
    }
}