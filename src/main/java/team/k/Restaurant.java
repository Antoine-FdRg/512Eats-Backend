package team.k;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Restaurant {
    private String name;
    private int id;
    private String open;
    private String close;

    public boolean isAvailable(){
        return true; // TODO: missing this implementation
    }
}
