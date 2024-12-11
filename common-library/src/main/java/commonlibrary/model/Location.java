package commonlibrary.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import commonlibrary.dto.LocationDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a restaurant or client location in the database.
 */
@Getter
@Setter
@EqualsAndHashCode
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@NoArgsConstructor
@Entity
@Table(name = "location")
public class Location {
    @Id
    private int id;

    private String streetNumber;

    private String address;

    private String city;

    private Location(Builder builder) {
        this.id = builder.id;
        this.streetNumber = builder.streetNumber;
        this.address = builder.address;
        this.city = builder.city;
    }

    public String toString() {
        return "Location [id=" + id + ", number=" + streetNumber + ", address=" + address + ", city=" + city + "]";
    }

    public LocationDTO convertLocationToLocationDto() {
        return new LocationDTO(id, streetNumber, address, city);
    }

    public static class Builder {
        private int id;
        private String streetNumber;
        private String address;
        private String city;

        private static int idCounter = 0;

        public Builder() {
            id = idCounter++;
        }

        public Builder setNumber(String streetNumber) {
            this.streetNumber = streetNumber;
            return this;
        }

        public Builder setAddress(String address) {
            this.address = address;
            return this;
        }

        public Builder setCity(String city) {
            this.city = city;
            return this;
        }

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public Location build() {
            return new Location(this);
        }

    }
}
