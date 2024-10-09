package team.k.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import team.k.common.Location;
import team.k.enumerations.OrderStatus;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GroupOrder {
    private final int id;
    private Date date;
    private OrderStatus status;
    private List<SubOrder> subOrders;
    private Location deliveryLocation;

    private GroupOrder(Builder builder) {
        this.id = builder.id;
        this.date = builder.date;
        this.status = builder.status;
        this.subOrders = builder.subOrders;
        this.deliveryLocation = builder.deliveryLocation;
    }

    public boolean addSubOrder(SubOrder subOrder) {
        return subOrders.add(subOrder);
    }

    public void close() {
        status = OrderStatus.CANCELED;
        this.subOrders.forEach(SubOrder::cancel);
    }

    public static class Builder {
        private static int nextId = 0;
        private final int id;
        private Date date;
        private final OrderStatus status;
        private List<SubOrder> subOrders;
        private Location deliveryLocation;

        public Builder() {
            this.id = nextId++;
            this.status = OrderStatus.CREATED;
        }

        public Builder withDate(Date date) {
            this.date = date;
            return this;
        }

        public Builder withSubOrders(List<SubOrder> subOrders) {
            this.subOrders = subOrders;
            return this;
        }

        public Builder withDeliveryLocation(Location deliveryLocation) {
            this.deliveryLocation = deliveryLocation;
            return this;
        }

        public GroupOrder build() {
            return new GroupOrder(this);
        }
    }
}
