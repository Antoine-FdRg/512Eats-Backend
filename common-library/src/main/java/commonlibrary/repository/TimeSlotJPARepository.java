package commonlibrary.repository;

import commonlibrary.model.restaurant.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeSlotJPARepository extends JpaRepository<TimeSlot, Long> {
    // Méthodes CRUD standard fournies automatiquement par Spring Data
    // Tu peux ajouter des requêtes personnalisées si nécessaire
}
