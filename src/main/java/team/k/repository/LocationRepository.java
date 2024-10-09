package team.k.repository;

import lombok.Getter;
import lombok.NoArgsConstructor;
import team.k.common.Location;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class LocationRepository {
    List<Location> locations = new ArrayList<>();

    /**
     * Returns a location by its id.
     *
     * @param id the id of the location to return.
     * @return the location with the given id, or null if no such location exists.
     */
    public Location findLocationById(int id) {
        return locations.stream().filter(location -> location.getId() == id).findFirst().orElse(null);
    }

}
