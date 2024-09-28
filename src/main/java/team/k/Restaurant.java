package team.k;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Restaurant {
    private String name;
    private int id;
    private String open;
    private String close;
//    private List<TimeSlot> timeSlots;  TODO: à décommenter une fois la classe TimeSlot créée
//    private List<Dish> dishes;  TODO: à décommenter une fois la classe Dish créée

    public boolean isAvailable(){
        return true; // TODO: missing this implementation
    }
}
