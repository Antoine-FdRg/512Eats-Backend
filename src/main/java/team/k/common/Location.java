package team.k.common;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a restaurant or client location in the database.
 */
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class Location {

    private final int id;

    private int number;

    private String address;

    private String city;

    public String toString() {
        return "Location [id=" + id + ", number=" + number + ", address=" + address + ", city=" + city + "]";
    }
}
