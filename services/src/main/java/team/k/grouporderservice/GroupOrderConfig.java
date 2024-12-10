package team.k.grouporderservice;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = {"team.k.grouporderservice"}) // Scanne les modèles et repositories
public class GroupOrderConfig {

}
