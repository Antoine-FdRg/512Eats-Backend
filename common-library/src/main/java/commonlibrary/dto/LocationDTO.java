package commonlibrary.dto;

import commonlibrary.model.Location;

public record LocationDTO(int id, String streetNumber, String address, String city) {

    public Location convertLocationDtoToLocation() {
        return new Location.Builder()
                .setId(id)
                .setNumber(streetNumber)
                .setAddress(address)
                .setCity(city)
                .build();
    }
}
