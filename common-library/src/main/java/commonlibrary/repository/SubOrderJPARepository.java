package commonlibrary.repository;

import commonlibrary.model.order.SubOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubOrderJPARepository extends JpaRepository<SubOrder, Long> {
    // Méthodes CRUD standard fournies automatiquement par Spring Data
    // Tu peux ajouter des requêtes personnalisées si nécessaire
}
