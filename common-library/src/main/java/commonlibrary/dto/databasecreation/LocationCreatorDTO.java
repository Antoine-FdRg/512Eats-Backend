package commonlibrary.dto.databasecreation;

import commonlibrary.model.Location;

public record LocationCreatorDTO(String streetNumber, String address, String city) {

    public LocationCreatorDTO(Location location) {
        this(location.getStreetNumber(), location.getAddress(), location.getCity());
    }
    public Location toLocation() {
        return new Location.Builder()
                .setNumber(streetNumber)
                .setAddress(address)
                .setCity(city)
                .build();
    }


}
