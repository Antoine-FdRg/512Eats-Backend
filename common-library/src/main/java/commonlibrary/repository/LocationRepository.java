package commonlibrary.repository;

import commonlibrary.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
    // Méthodes CRUD standard fournies automatiquement par Spring Data
    // Tu peux ajouter des requêtes personnalisées si nécessaire
}
