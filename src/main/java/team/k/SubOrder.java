package team.k;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubOrder {
    private int id;
    private double price;
    private GroupOrder groupOrder;
    private Restaurant restaurant;
    private RegisteredUser user;
//    private List<Dish> dishes; TODO: à décommenter une fois la classe Dish créée
    /* méthod à décommenter une fois la classe Dish créée
    public Dish getCheaperDish(){
        return new Dish();
    }
     */
}
