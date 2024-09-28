package team.k;

/**
 * Represents a restaurant or client location in the database.
 */
public class Location {
    private int id;
    private int number;
    private String address;
    private String city;

    public Location(int id, int number, String address, String city) {
        this.id = id;
        this.number = number;
        this.address = address;
        this.city = city;
    }

    public int getId() {
        return id;
    }

    public int getNumber() {
        return number;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String toString() {
        return "Location [id=" + id + ", number=" + number + ", address=" + address + ", city=" + city + "]";
    }
}
