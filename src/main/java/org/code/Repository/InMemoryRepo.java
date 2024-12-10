package org.code.Repository;

import org.code.Entities.HasId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryRepo<T> implements IRepository<T> {
    private Map<Integer, T> storage = new HashMap<>();
    private int currentId = 1;

    @Override
    public void create(T obj) {
    if(obj instanceof HasId) {
        HasId entity = (HasId) obj;
        entity.setId(currentId++);
        storage.put(entity.getId(), obj);
    } else {
        throw new IllegalArgumentException("Object does not have an ID");
    }
    }

    @Override
    public T read(int id) {
        return storage.get(id);
    }

    @Override
    public List<T> readAll() {
        return List.of();
    }

    @Override
    public void update(T obj) {
        if(obj instanceof HasId) {
            HasId entity = (HasId) obj;
            if(storage.containsKey(entity.getId())) {
                storage.put(entity.getId(), obj);
            } else {
                throw new IllegalArgumentException("Object does not exist in storage");
            }
        } else {
            throw new IllegalArgumentException("Object does not have an ID");
        }
    }

    @Override
    public void delete(int id) {
        if(storage.containsKey(id)) {
            storage.remove(id);
        } else {
            throw new IllegalArgumentException("Object does not exist in storage");
        }
    }

    @Override
    public List<T> getAll() {
        return new ArrayList<>(storage.values());
    }
}
