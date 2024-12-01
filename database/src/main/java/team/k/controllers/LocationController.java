package team.k.controllers;

import commonlibrary.dto.databasecreation.LocationCreatorDTO;
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
        LocationRepository.throwIfLocationIdDoesNotExist(id);
        return LocationRepository.getInstance().findById(id);
    }

    @Endpoint(path = "/create", method = HttpMethod.POST)
    @Response(status = 201, message = "Location created successfully")
    public Location add(@RequestBody LocationCreatorDTO locationCreatorDTO) {
        Location location = locationCreatorDTO.toLocation();
        LocationRepository.getInstance().add(location);
        return location;
    }

    @Endpoint(path = "/update", method = HttpMethod.PUT)
    @Response(status = 200, message = "Location updated successfully")
    public void update(@RequestBody Location location) throws SSDBQueryProcessingException {
        LocationRepository.throwIfLocationIdDoesNotExist(location.getId());
        Location existingLocation = LocationRepository.getInstance().findById(location.getId());
        LocationRepository.getInstance().update(location, existingLocation);
    }

    @Endpoint(path = "/delete/{id}", method = HttpMethod.DELETE)
    @Response(status = 200, message = "Location deleted successfully")
    public void remove(@PathVariable("id") int id) throws SSDBQueryProcessingException {
        LocationRepository.throwIfLocationIdDoesNotExist(id);
        LocationRepository.getInstance().remove(id);
    }

}
