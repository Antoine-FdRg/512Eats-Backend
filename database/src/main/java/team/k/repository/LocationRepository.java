package team.k.repository;

import commonlibrary.model.Location;


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
}
