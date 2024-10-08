package team.k.common;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a restaurant or client location in the database.
 */
@Getter
@Setter
@EqualsAndHashCode
public class Location {

    private final int id;

    private int number;

    private String address;

    private String city;

    Location(int id, int number, String address, String city) {
        this.id = id;
        this.number = number;
        this.address = address;
        this.city = city;
    }

    public String toString() {
        return "Location [id=" + id + ", number=" + number + ", address=" + address + ", city=" + city + "]";
    }
}
