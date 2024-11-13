package team.k;

import io.ebean.Database;
import io.ebean.DatabaseFactory;
import io.ebean.config.DatabaseConfig;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        DatabaseConfig config = new DatabaseConfig();
        config.loadFromProperties();
        Database database = DatabaseFactory.create(config);
        Dish dish = new Dish.Builder()
                .setName("Zappi")
                .setDescription("A delicious pizza")
                .setPrice(10.0)
                .build();
        database.save(dish);


    }
}