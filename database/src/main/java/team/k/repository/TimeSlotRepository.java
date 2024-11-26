package team.k.repository;

import commonlibrary.model.restaurant.TimeSlot;

public class TimeSlotRepository extends GenericRepository<TimeSlot> {
    private static TimeSlotRepository instance;

    private TimeSlotRepository() {
        super();
    }

    public static TimeSlotRepository getInstance() {
        if (instance == null) {
            instance = new TimeSlotRepository();
        }
        return instance;
    }

    public TimeSlot findById(int timeSlotId) {
        return findAll().stream().filter(restaurant -> restaurant.getId() == timeSlotId).findFirst().orElse(null);
    }
}
