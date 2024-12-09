package commonlibrary.repository;

import commonlibrary.model.order.IndividualOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IndividualOrderRepository extends JpaRepository<IndividualOrder, Long> {
    // Méthodes CRUD standard fournies automatiquement par Spring Data
    // Tu peux ajouter des requêtes personnalisées si nécessaire
}
