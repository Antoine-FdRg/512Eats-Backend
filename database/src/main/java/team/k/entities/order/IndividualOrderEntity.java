package team.k.entities.order;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import team.k.entities.LocationEntity;

@Getter
@Setter
@Entity
@DiscriminatorValue("individual_order")
public class IndividualOrderEntity extends SubOrderEntity {
    @ManyToOne
    @JoinColumn(name = "delivery_location_id")
    private LocationEntity deliveryLocation;

    public IndividualOrderEntity(LocationEntity deliveryLocation) {
        super();
        this.deliveryLocation = deliveryLocation;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof IndividualOrderEntity individualOrder)) {
            return false;
        }
        return id == individualOrder.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
