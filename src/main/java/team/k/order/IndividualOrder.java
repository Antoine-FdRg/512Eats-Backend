package team.k.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import team.k.common.Location;

@Getter
@Setter
@AllArgsConstructor
public class IndividualOrder extends SubOrder {
    private Location deliveryLocation;
}
