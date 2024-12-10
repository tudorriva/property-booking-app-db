package org.code.Repository;

import java.util.List;

public interface IRepository<T> {
    void create(T obj);
    T read(int id);

    List<T> readAll();

    void update(T obj);
    void delete(int id);
    List<T> getAll();
}
