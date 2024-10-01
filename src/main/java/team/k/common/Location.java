package team.k.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a restaurant or client location in the database.
 */
@Getter
@Setter
@AllArgsConstructor
public class Location {

    private int id;

    private int number;

    private String address;

    private String city;

    public String toString() {
        return "Location [id=" + id + ", number=" + number + ", address=" + address + ", city=" + city + "]";
    }
}
