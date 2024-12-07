package team.k.repository;

import commonlibrary.model.restaurant.TimeSlot;

import java.util.ArrayList;
import java.util.List;

public class TimeSlotRepository {
    private static final List<TimeSlot> timeSlots = new ArrayList<>();

    public static TimeSlot findById(int timeSlotId) {
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
