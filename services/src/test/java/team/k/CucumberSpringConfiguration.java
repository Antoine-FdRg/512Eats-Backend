package team.k;

import io.cucumber.spring.*;
import org.springframework.boot.test.context.SpringBootTest;
import team.k.grouporderservice.GroupOrderConfig;
import team.k.managementservice.ManagementConfig;
import team.k.orderservice.OrderConfig;
import team.k.restaurantservice.RestaurantConfig;

@CucumberContextConfiguration
@SpringBootTest(classes = {RestaurantConfig.class, ManagementConfig.class,TestConfig.class, OrderConfig.class, GroupOrderConfig.class}) // Charge le contexte Spring Boot
public class CucumberSpringConfiguration {
}
