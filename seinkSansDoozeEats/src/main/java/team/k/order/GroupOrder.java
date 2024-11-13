package team.k.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import team.k.common.Location;
import team.k.enumerations.OrderStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GroupOrder {
    static final int GROUP_ORDER_CODE_LENGTH = 6;
    private final int id;
    private LocalDateTime deliveryDateTime;
    private OrderStatus status;
    private List<SubOrder> subOrders;
    private final Location deliveryLocation;

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

    public void place(LocalDateTime now) {
        boolean atLeastOneSuborderisPaid = false;
        for (SubOrder subOrder : this.getSubOrders()) {
            if (subOrder.getStatus() == OrderStatus.PAID) {
                subOrder.place(now);
                atLeastOneSuborderisPaid = true;
            } else {
                subOrder.cancel();
            }
        }
        if (atLeastOneSuborderisPaid) {
            this.setStatus(OrderStatus.PLACED);
        } else {
            this.setStatus(OrderStatus.CANCELED);
        }
    }

    public static class Builder {
        private static int nextId = 0;
        private final int id;
        private LocalDateTime deliveryDateTime;
        private final OrderStatus status;
        private List<SubOrder> subOrders;
        private Location deliveryLocation;

        public Builder() {
            this.id = generateId(nextId++);
            this.status = OrderStatus.CREATED;
            this.subOrders = new ArrayList<>();
        }

        /**
         * Generate a non-trivial number of 6 digits for the group
         *
         * @param seed the seed to generate the id
         */
        static int generateId(int seed) {
            int transformed = (seed * 31 + 7) ^ 0x5A5A5A5A;
            int hash = Integer.hashCode(transformed);
            int identifier = (int) (Math.abs(hash) % Math.pow(10, GROUP_ORDER_CODE_LENGTH));
            int maxNbOfIterations = 50;
            while (identifier < Math.pow(10, GROUP_ORDER_CODE_LENGTH - 1.0) && maxNbOfIterations > 0) {
                identifier *= 10;
                maxNbOfIterations--;
            }
            return identifier;
        }

        public Builder withDate(LocalDateTime date) {
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
