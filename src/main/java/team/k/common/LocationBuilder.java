package team.k.common;

public class LocationBuilder {
    private int id;
    private int number;
    private String address;
    private String city;

    private static int idCounter = 0;

    public LocationBuilder() {
        id = idCounter++;
    }

    public LocationBuilder setNumber(int number) {
        this.number = number;
        return this;
    }

    public LocationBuilder setAddress(String address) {
        this.address = address;
        return this;
    }

    public LocationBuilder setCity(String city) {
        this.city = city;
        return this;
    }

    public Location build() {
        return new Location(id, number, address, city);
    }
}
