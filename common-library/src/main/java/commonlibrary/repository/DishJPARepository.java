package commonlibrary.repository;

import commonlibrary.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DishJPARepository extends JpaRepository<Dish, Long> {
    // Méthodes CRUD standard fournies automatiquement par Spring Data
    // Tu peux ajouter des requêtes personnalisées si nécessaire
}
