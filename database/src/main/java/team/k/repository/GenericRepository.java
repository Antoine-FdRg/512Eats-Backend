package team.k.repository;

import java.util.ArrayList;
import java.util.List;

public abstract class GenericRepository <T> {
    private final List<T> items;

    public GenericRepository(){
        items = new ArrayList<>();
    }


    public List<T> findAll(){
        return new ArrayList<>(items);
    }

    public List<T> getItems(int limit, int offset){
        return items.subList(offset, limit);
    }

    public abstract T findById(int id);

    public void add(T t){
        items.add(t);
    }

    public boolean update(T t){
        int index = items.indexOf(t);
        if(index != -1){
            items.set(index, t);
        }
        return index != -1;
    }

    public boolean remove(int id){
        return items.remove(findById(id));
    }
}