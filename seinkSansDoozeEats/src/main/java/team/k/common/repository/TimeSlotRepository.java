package team.k.common.repository;

import team.k.common.model.restaurant.TimeSlot;

import java.util.ArrayList;
import java.util.List;

public class TimeSlotRepository {
    private final List<TimeSlot> timeSlots = new ArrayList<>();

    public TimeSlot findById(int timeSlotId) {
        return timeSlots.stream().filter(restaurant -> restaurant.getId() == timeSlotId).findFirst().orElse(null);
    }

    public void add(TimeSlot timeSlot) {
        timeSlots.add(timeSlot);
    }

    public void removeTimeSlot(TimeSlot timeSlot) {
        timeSlots.remove(timeSlot);
    }

    public List<TimeSlot> findAll() {
        return timeSlots;
    }

    public void clear() {
        timeSlots.clear();
    }
}
