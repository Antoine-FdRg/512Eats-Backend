package team.k.repository;

import commonlibrary.model.order.SubOrder;
import ssdbrestframework.SSDBQueryProcessingException;

import java.util.Objects;

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
        return findAll().stream().filter(subOrder -> subOrder.getUserID() == id).findFirst().orElse(null);
    }


    public static void throwIfSubOrderIdDoesNotExist(int subOrderID) throws SSDBQueryProcessingException {
        if (Objects.isNull(SubOrderRepository.getInstance().findById(subOrderID))) {
            throw new SSDBQueryProcessingException(404, "Suborder with ID " + subOrderID + " not found.");
        }
    }
}
