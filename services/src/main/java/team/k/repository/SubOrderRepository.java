package team.k.repository;


import commonlibrary.model.order.SubOrder;

import java.util.ArrayList;
import java.util.List;

public class SubOrderRepository {

    private static final List<SubOrder> subOrders = new ArrayList<>();

    public static void add(SubOrder subOrder) {
        subOrders.add(subOrder);
    }

    public static SubOrder findById(int id) {
        return subOrders.stream().filter(subOrder -> subOrder.getId() == id).findFirst().orElse(null);
    }

    public List<SubOrder> findAll() {
        return subOrders;
    }

    public void delete(SubOrder subOrder) {
        subOrders.remove(subOrder);
    }

    public static void clear() {
        subOrders.clear();
    }

    public SubOrder findByUserId(int id) {
        return subOrders.stream().filter(subOrder -> subOrder.getUserID() == id).findFirst().orElse(null);
    }
}
