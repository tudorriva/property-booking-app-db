package org.code.Repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.code.Entities.HasId;
import org.hibernate.exception.ConstraintViolationException;

import java.util.List;

/**
 * The {@code DBRepository} class provides a base implementation of the {@code IRepository} interface
 * for repositories interacting with a database. It defines common database operations.
 *
 * @param <T> The type of entity managed by the repository.
 */
public class DBRepository<T extends HasId> implements IRepository<T> {
    private final SessionFactory sessionFactory;
    private final Class<T> entityType;

    public DBRepository(SessionFactory sessionFactory, Class<T> entityType) {
        this.sessionFactory = sessionFactory;
        this.entityType = entityType;
    }

    @Override
    public void create(T entity) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.save(entity); // Save new entity
            transaction.commit();
        } catch (ConstraintViolationException e) {
            transaction.rollback();
            System.err.println("Constraint violation: " + e.getConstraintName());
            throw new RuntimeException("Error creating entity in the database. Constraint violation: " + e.getConstraintName(), e);
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Error creating entity in the database.", e);
        } finally {
            session.close();
        }
    }

    @Override
    public T read(int id) {
        Session session = sessionFactory.openSession();
        try {
            return session.get(entityType, id); // Fetch the entity by ID
        } catch (Exception e) {
            throw new RuntimeException("Error reading entity in the database.", e);
        } finally {
            session.close();
        }
    }

    @Override
    public List<T> readAll() {
        return List.of();
    }

    @Override
    public void update(T entity) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            T existingEntity = session.get(entityType, entity.getId()); // Get existing entity
            if (existingEntity != null) {
                session.merge(entity); // Update the existing entity with new data
            }
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Error updating entity in the database", e);
        } finally {
            session.close();
        }
    }

    @Override
    public void delete(int id) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            T entity = session.get(entityType, id); // Get the entity by ID
            if (entity != null) {
                session.delete(entity); // Delete the entity
            }
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Error deleting entity in the database.", e);
        } finally {
            session.close();
        }
    }

    @Override
    public List<T> getAll() {
        Session session = sessionFactory.openSession();
        try {
            String query = String.format("FROM %s", entityType.getSimpleName()); // HQL query
            return session.createQuery(query, entityType).getResultList(); // Retrieve all entities
        } catch (Exception e) {
            throw new RuntimeException("Error reading all the entities in the database.", e);
        } finally {
            session.close();
        }
    }
}