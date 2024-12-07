package team.k.repository;


import commonlibrary.model.order.IndividualOrder;

import java.util.ArrayList;
import java.util.List;

public class IndividualOrderRepository {

    private static final List<IndividualOrder> individualOrders = new ArrayList<>();

    public static void add(IndividualOrder individualOrder) {
        individualOrders.add(individualOrder);
    }

    public static List<IndividualOrder> findAll() {
        return individualOrders;
    }

    public static IndividualOrder findById(int id) {
        return individualOrders.stream()
                .filter(individualOrder -> individualOrder.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void delete(IndividualOrder individualOrder) {
        individualOrders.remove(individualOrder);
    }

    public static void clear() {
        individualOrders.clear();
    }
}
