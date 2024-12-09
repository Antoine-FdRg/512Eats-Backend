package commonlibrary.repository;

import commonlibrary.model.restaurant.discount.DiscountStrategy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountStrategyRepository extends JpaRepository<DiscountStrategy, Long> {
    // Méthodes CRUD standard fournies automatiquement par Spring Data
    // Tu peux ajouter des requêtes personnalisées si nécessaire
}
