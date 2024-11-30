package team.k.repository;

import ssdbrestframework.SSDBQueryProcessingException;
import team.k.models.PersistedGroupOrder;

public class GroupOrderRepository extends GenericRepository<PersistedGroupOrder> {

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

    public PersistedGroupOrder findById(int id) {
        return findAll().stream().filter(groupOrder -> groupOrder.getId() == id).findFirst().orElse(null);
    }

    public static void throwIfGroupOrderIdDoesNotExist(int id) throws SSDBQueryProcessingException {
        if (getInstance().findById(id) == null) {
            throw new SSDBQueryProcessingException(404, "Group Order with ID " + id + " not found.");
        }
    }
}
