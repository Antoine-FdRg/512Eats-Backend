package team.k.api;


import lombok.Data;

@Data
public class MyData {
    private String name;
    private int age;

    @Override
    public String toString() {
        return "MyData{name='" + name + "', age=" + age + "}";
    }
}
