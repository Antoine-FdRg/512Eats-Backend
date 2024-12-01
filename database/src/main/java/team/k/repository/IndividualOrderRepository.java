package team.k.repository;

import commonlibrary.model.order.IndividualOrder;
import ssdbrestframework.SSDBQueryProcessingException;

import java.util.Objects;

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

    public static void throwIfIndividualOrderIdDoesNotExist(int subOrderID) throws SSDBQueryProcessingException {
        if (Objects.isNull(IndividualOrderRepository.getInstance().findById(subOrderID))) {
            throw new SSDBQueryProcessingException(404, "Suborder with ID " + subOrderID + " not found.");
        }
    }
}
