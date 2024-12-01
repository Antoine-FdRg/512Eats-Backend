package team.k.repository;

import commonlibrary.model.Location;
import ssdbrestframework.SSDBQueryProcessingException;

import java.util.Objects;


public class LocationRepository extends GenericRepository<Location> {
    private static LocationRepository instance;

    private LocationRepository() {
        super();
    }

    public static LocationRepository getInstance() {
        if (instance == null) {
            instance = new LocationRepository();
        }
        return instance;
    }

    public Location findById(int id) {
        return findAll().stream().filter(location -> location.getId() == id).findFirst().orElse(null);
    }

    public static void throwIfLocationIdDoesNotExist(int locationID) throws SSDBQueryProcessingException {
        if (Objects.isNull(LocationRepository.getInstance().findById(locationID))) {
            throw new SSDBQueryProcessingException(404, "Location with ID " + locationID + " not found.");
        }
    }
}
