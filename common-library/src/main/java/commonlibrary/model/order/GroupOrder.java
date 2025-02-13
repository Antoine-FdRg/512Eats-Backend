package commonlibrary.model.order;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import commonlibrary.dto.GroupOrderDTO;
import commonlibrary.dto.SubOrderDTO;
import commonlibrary.enumerations.OrderStatus;
import commonlibrary.model.Location;
import commonlibrary.model.RegisteredUser;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@NoArgsConstructor
@Entity
@Table(name = "group_order")
public class GroupOrder {
    public static final int GROUP_ORDER_CODE_LENGTH = 6;
    @Id
    private int id;
    private LocalDateTime deliveryDateTime;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    @OneToMany(mappedBy = "groupOrder", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<SubOrder> subOrders;
    private int deliveryLocationID;

    private GroupOrder(Builder builder) {
        this.id = builder.id;
        this.deliveryDateTime = builder.deliveryDateTime;
        this.status = builder.status;
        this.subOrders = builder.subOrders;
        this.deliveryLocationID = builder.deliveryLocationID;
    }

    public boolean addSubOrder(SubOrder subOrder) {
        return subOrders.add(subOrder);
    }

    public void close() {
        status = OrderStatus.CANCELED;
        this.subOrders.forEach(SubOrder::cancel);
    }

    @Transactional
    public void place(LocalDateTime now, List<RegisteredUser> members) {
        boolean atLeastOneSuborderisPaid = false;
        for (SubOrder subOrder : this.getSubOrders()) {
            RegisteredUser orderOwner = members.stream()
                    .filter(member -> member.getId() == subOrder.getUserID())
                    .findFirst()
                    .orElseThrow();
            if (subOrder.getStatus() == OrderStatus.PAID) {
                subOrder.place(now, orderOwner);
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

    public GroupOrderDTO convertGroupOrderToGroupOrderDto(Location location) {
        List<SubOrderDTO> convertedSubOrders = subOrders.stream()
                .map(SubOrder::convertSubOrderToSubOrderDto)
                .toList();
        return new GroupOrderDTO(id,
                status.toString(),
                location,
                deliveryDateTime != null ? deliveryDateTime.toString() : "",
                convertedSubOrders);
    }

    public static class Builder {
        private static int nextId = 0;
        private int id;
        private LocalDateTime deliveryDateTime;
        private OrderStatus status;
        private List<SubOrder> subOrders;
        private int deliveryLocationID;

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
        public static int generateId(int seed) {
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

        public Builder withDeliveryLocationID(int deliveryLocationID) {
            this.deliveryLocationID = deliveryLocationID;
            return this;
        }

        public Builder withStatus(OrderStatus status) {
            this.status = status;
            return this;
        }

        public Builder withId(int id) {
            this.id = id;
            return this;
        }


        public GroupOrder build() {
            return new GroupOrder(this);
        }


    }
}
