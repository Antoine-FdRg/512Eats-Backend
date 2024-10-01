package team.k.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import team.k.common.Location;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class IndividualOrder extends SubOrder {
    private Date date;
    private Location deliveryLocation;
}
