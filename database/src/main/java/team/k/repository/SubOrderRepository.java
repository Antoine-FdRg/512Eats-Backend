package team.k.repository;

import commonlibrary.model.order.SubOrder;

public class SubOrderRepository extends GenericRepository<SubOrder> {
    private static SubOrderRepository instance;

    private SubOrderRepository() {
        super();
    }

    public static SubOrderRepository getInstance() {
        if (instance == null) {
            instance = new SubOrderRepository();
        }
        return instance;
    }

    public SubOrder findById(int id) {
        return findAll().stream().filter(subOrder -> subOrder.getId() == id).findFirst().orElse(null);
    }

    public SubOrder findByUserId(int id) {
        return findAll().stream().filter(subOrder -> subOrder.getUser().getId() == id).findFirst().orElse(null);
    }
}
