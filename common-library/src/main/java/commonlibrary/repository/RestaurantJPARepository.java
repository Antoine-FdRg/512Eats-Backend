package commonlibrary.repository;

import commonlibrary.model.restaurant.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantJPARepository extends JpaRepository<Restaurant, Long> {
    List<Restaurant> findByName(String restaurantName);
    // Méthodes CRUD standard fournies automatiquement par Spring Data
    // Tu peux ajouter des requêtes personnalisées si nécessaire
}
