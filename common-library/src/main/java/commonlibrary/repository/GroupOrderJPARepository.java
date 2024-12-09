package commonlibrary.repository;

import commonlibrary.model.order.GroupOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupOrderJPARepository extends JpaRepository<GroupOrder, Long> {
    // Méthodes CRUD standard fournies automatiquement par Spring Data
    // Tu peux ajouter des requêtes personnalisées si nécessaire
}
