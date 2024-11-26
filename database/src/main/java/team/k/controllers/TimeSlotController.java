package team.k.controllers;

import commonlibrary.model.restaurant.TimeSlot;
import ssdbrestframework.HttpMethod;
import ssdbrestframework.SSDBQueryProcessingException;
import ssdbrestframework.annotations.Endpoint;
import ssdbrestframework.annotations.PathVariable;
import ssdbrestframework.annotations.RequestBody;
import ssdbrestframework.annotations.Response;
import ssdbrestframework.annotations.RestController;
import team.k.repository.TimeSlotRepository;

import java.util.List;

@RestController(path = "/timeslots")
public class TimeSlotController {

    @Endpoint(path = "", method = HttpMethod.GET)
    public List<TimeSlot> findAll() {
        return TimeSlotRepository.getInstance().findAll();
    }

    @Endpoint(path = "/get/{id}", method = HttpMethod.GET)
    public TimeSlot findById(@PathVariable("id") int id) throws SSDBQueryProcessingException {
        TimeSlot timeSlot = TimeSlotRepository.getInstance().findById(id);
        if(timeSlot==null){
            throw new SSDBQueryProcessingException(404, "TimeSlot with ID " + id + " not found.");
        }
        return timeSlot;
    }

    @Endpoint(path = "/create", method = HttpMethod.POST)
    @Response(status = 201, message = "TimeSlot created successfully")
    public void add(@RequestBody TimeSlot timeSlot) throws SSDBQueryProcessingException {
        if(TimeSlotRepository.getInstance().findById(timeSlot.getId()) != null){
            throw new SSDBQueryProcessingException(409, "TimeSlot with ID " + timeSlot.getId() + " already exists, try updating it instead.");
        }
        TimeSlotRepository.getInstance().add(timeSlot);
    }

    @Endpoint(path = "/update", method = HttpMethod.PUT)
    @Response(status = 200, message = "TimeSlot updated successfully")
    public void update(@RequestBody TimeSlot timeSlot) throws SSDBQueryProcessingException {
        TimeSlot existingTimeSlot = TimeSlotRepository.getInstance().findById(timeSlot.getId());
        if(existingTimeSlot == null){
            throw new SSDBQueryProcessingException(404, "TimeSlot with ID " + timeSlot.getId() + " not found, try creating it instead.");
        }
        TimeSlotRepository.getInstance().update(timeSlot, existingTimeSlot);
    }

    @Endpoint(path = "/delete/{id}", method = HttpMethod.DELETE)
    @Response(status = 200, message = "TimeSlot deleted successfully")
    public void remove(@PathVariable("id") int id) throws SSDBQueryProcessingException {
        boolean success = TimeSlotRepository.getInstance().remove(id);
        if(!success){
            throw new SSDBQueryProcessingException(404, "TimeSlot with ID " + id + " not found.");
        }
    }
}
