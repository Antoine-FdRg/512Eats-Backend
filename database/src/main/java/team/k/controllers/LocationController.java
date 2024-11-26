package team.k.controllers;

import commonlibrary.model.Location;
import ssdbrestframework.HttpMethod;
import ssdbrestframework.SSDBQueryProcessingException;
import ssdbrestframework.annotations.Endpoint;
import ssdbrestframework.annotations.PathVariable;
import ssdbrestframework.annotations.RequestBody;
import ssdbrestframework.annotations.Response;
import ssdbrestframework.annotations.RestController;
import team.k.repository.LocationRepository;

import java.util.List;

@RestController(path = "/locations")
public class LocationController {
    @Endpoint(path = "", method = HttpMethod.GET)
    public List<Location> findAll() {
        return LocationRepository.getInstance().findAll();
    }

    @Endpoint(path = "/get/{id}", method = HttpMethod.GET)
    public Location findById(@PathVariable("id") int id) throws SSDBQueryProcessingException {
        Location location = LocationRepository.getInstance().findById(id);
        if (location == null) {
            throw new SSDBQueryProcessingException(404, "Location with ID " + id + " not found.");
        }
        return location;
    }

    @Endpoint(path = "/create", method = HttpMethod.POST)
    @Response(status = 201, message = "Location created successfully")
    public void add(@RequestBody Location location) throws SSDBQueryProcessingException {
        if (LocationRepository.getInstance().findById(location.getId()) != null) {
            throw new SSDBQueryProcessingException(409, "Location with ID " + location.getId() + " already exists, try updating it instead.");
        }
        LocationRepository.getInstance().add(location);
    }

    @Endpoint(path = "/update", method = HttpMethod.PUT)
    @Response(status = 200, message = "Location updated successfully")
    public void update(@RequestBody Location location) throws SSDBQueryProcessingException {
        Location existingLocation = LocationRepository.getInstance().findById(location.getId());
        if (existingLocation == null) {
            throw new SSDBQueryProcessingException(404, "Location with ID " + location.getId() + " not found, try creating it instead.");
        }
        LocationRepository.getInstance().update(location, existingLocation);
    }

    @Endpoint(path = "/delete/{id}", method = HttpMethod.DELETE)
    @Response(status = 200, message = "Location deleted successfully")
    public void remove(@PathVariable("id") int id) throws SSDBQueryProcessingException {
        boolean success = LocationRepository.getInstance().remove(id);
        if (!success) {
            throw new SSDBQueryProcessingException(404, "Location with ID " + id + " not found.");
        }
    }

}
