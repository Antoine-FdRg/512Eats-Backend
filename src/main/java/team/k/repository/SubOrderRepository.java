package team.k.repository;

import team.k.order.SubOrder;

import java.util.ArrayList;
import java.util.List;

public class SubOrderRepository {

    private final List<SubOrder> subOrders = new ArrayList<>();

    public void save(SubOrder subOrder) {
        subOrders.add(subOrder);
    }

    public SubOrder findById(int id) {
        return subOrders.stream().filter(subOrder -> subOrder.getId() == id).findFirst().orElse(null);
    }

    public List<SubOrder> findAll() {
        return subOrders;
    }

    public void delete(SubOrder subOrder) {
        subOrders.remove(subOrder);
    }

    public void clear() {
        subOrders.clear();
    }
}
