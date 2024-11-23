package team.k.entities;

import io.ebean.Model;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a restaurant or client location in the database.
 */
@Getter
@Setter
@Entity
@Table(name = "location")
@NoArgsConstructor
public class LocationEntity extends Model {

    @Id
    private int id;

    private String streetNumber;

    private String address;

    private String city;

    private LocationEntity(Builder builder) {
        this.id = builder.id;
        this.streetNumber = builder.streetNumber;
        this.address = builder.address;
        this.city = builder.city;
    }

    public String toString() {
        return "Location [id=" + id + ", number=" + streetNumber + ", address=" + address + ", city=" + city + "]";
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

        public LocationEntity build() {
            return new LocationEntity(this);
        }

    }


    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof LocationEntity location)) {
            return false;
        }
        return id == location.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
