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
    private Date deliveryDateTime;
    private OrderStatus status;
    private List<SubOrder> subOrders;
    private Location deliveryLocation;

    private GroupOrder(Builder builder) {
        this.id = builder.id;
        this.deliveryDateTime = builder.deliveryDateTime;
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
        private Date deliveryDateTime;
        private final OrderStatus status;
        private List<SubOrder> subOrders;
        private Location deliveryLocation;

        public Builder() {
            this.id = generateId(nextId++);
            this.status = OrderStatus.CREATED;
        }

        /**
         * Generate a non-trivial of 6 digits for the group
         * @param seed the seed to generate the id
         */
        private static int generateId(int seed) {
            int transformed = (seed * 31 + 7) ^ 0x5A5A5A5A;
            int hash = Integer.hashCode(transformed);
            int identifier = Math.abs(hash) % 1000000;
            int maxNbOfIterations = 50;
            while (identifier < 100000 && maxNbOfIterations > 0) {
                identifier *= 10;
                maxNbOfIterations--;
            }
            return identifier;
        }

        public Builder withDate(Date date) {
            this.deliveryDateTime = date;
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
