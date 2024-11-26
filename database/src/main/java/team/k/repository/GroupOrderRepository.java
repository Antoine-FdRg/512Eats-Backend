package team.k.repository;

import commonlibrary.model.order.GroupOrder;

public class GroupOrderRepository extends GenericRepository<GroupOrder> {

    private static GroupOrderRepository instance;

    private GroupOrderRepository() {
        super();
    }

    public static GroupOrderRepository getInstance() {
        if (instance == null) {
            instance = new GroupOrderRepository();
        }
        return instance;
    }

    public GroupOrder findById(int id) {
        return findAll().stream().filter(groupOrder -> groupOrder.getId() == id).findFirst().orElse(null);
    }
}
