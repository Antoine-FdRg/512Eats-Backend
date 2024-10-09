package team.k.repository;

import team.k.order.IndividualOrder;

import java.util.ArrayList;
import java.util.List;

public class IndividualOrderRepository {

    private final List<IndividualOrder> individualOrders = new ArrayList<>();

    public void add(IndividualOrder individualOrder) {
        individualOrders.add(individualOrder);
    }

    public List<IndividualOrder> findAll() {
        return individualOrders;
    }

    public IndividualOrder findById(int id) {
        return individualOrders.stream()
                .filter(individualOrder -> individualOrder.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void delete(IndividualOrder individualOrder) {
        individualOrders.remove(individualOrder);
    }


}
