package team.k;

import lombok.extern.slf4j.Slf4j;
import ssdbrestframework.HttpMethod;
import ssdbrestframework.SSDBQueryProcessingException;
import ssdbrestframework.annotations.Endpoint;
import ssdbrestframework.annotations.PathVariable;

import java.util.List;

import static team.k.DatabaseServer.database;

@Slf4j
public abstract class GenericController<T> {

    private final Class<T> modelClass;

    protected GenericController(Class<T> modelClass) {
        this.modelClass = modelClass;
    }

    @Endpoint(path = "/", method = HttpMethod.GET)
    public List<T> findAll() {
        log.info("Finding all " + modelClass + "s");
        List<T> entities = database.find(modelClass).findList();
        return entities;
    }

    @Endpoint(path = "/{id}", method = HttpMethod.GET)
    public T findById(@PathVariable("id") int id) throws SSDBQueryProcessingException {
        log.info("Finding " + modelClass + " with ID " + id);
        T object = database.find(modelClass).where().eq("id", id).findOne();
        if (object == null) {
            throw new SSDBQueryProcessingException(404, modelClass + " with ID " + id + " not found.");
        }
        return database.find(modelClass).where().eq("id", id).findOne();
    }
}
