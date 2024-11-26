package team.k.repository;

import commonlibrary.model.order.IndividualOrder;

public class IndividualOrderRepository extends GenericRepository<IndividualOrder> {
    private static IndividualOrderRepository instance;

    private IndividualOrderRepository() {
        super();
    }

    public static IndividualOrderRepository getInstance() {
        if (instance == null) {
            instance = new IndividualOrderRepository();
        }
        return instance;
    }

    public IndividualOrder findById(int id) {
        return findAll().stream().filter(individualOrder -> individualOrder.getId() == id).findFirst().orElse(null);
    }


}
