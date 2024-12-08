package team.k.repository;

import commonlibrary.model.restaurant.TimeSlot;

import java.util.ArrayList;
import java.util.List;

public class TimeSlotRepository {
    private TimeSlotRepository() {
        throw new IllegalStateException("Utility class");
    }

    private static final List<TimeSlot> timeSlots = new ArrayList<>();

    public static TimeSlot findById(int timeSlotId) {
        return timeSlots.stream().filter(restaurant -> restaurant.getId() == timeSlotId).findFirst().orElse(null);
    }

    public static void add(TimeSlot timeSlot) {
        timeSlots.add(timeSlot);
    }

    public static void removeTimeSlot(TimeSlot timeSlot) {
        timeSlots.remove(timeSlot);
    }

    public static List<TimeSlot> findAll() {
        return timeSlots;
    }

    public static void clear() {
        timeSlots.clear();
    }
}
