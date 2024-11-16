package team.k.api.examples;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyData {
    private String name;
    private double age;

    @Override
    public String toString() {
        return "MyData{name='" + name + "', age=" + age + "}";
    }
}
