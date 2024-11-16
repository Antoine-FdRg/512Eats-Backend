package ssdbrestframework.examples;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExampleData {
    private String name;
    private double age;

    @Override
    public String toString() {
        return "MyData{name='" + name + "', age=" + age + "}";
    }
}
