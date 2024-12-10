package org.code.Repository;

import org.code.Entities.HasId;
import org.code.Exceptions.DatabaseException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import java.util.List;

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
            session.save(entity);
            transaction.commit();
        } catch (ConstraintViolationException e) {
            transaction.rollback();
            throw new DatabaseException("Constraint violation: " + e.getConstraintName(), e);
        } catch (Exception e) {
            transaction.rollback();
            throw new DatabaseException("Error creating entity in the database.", e);
        } finally {
            session.close();
        }
    }

    @Override
    public T read(int id) {
        Session session = sessionFactory.openSession();
        try {
            return session.get(entityType, id);
        } catch (Exception e) {
            throw new DatabaseException("Error reading entity in the database.", e);
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
            T existingEntity = session.get(entityType, entity.getId());
            if (existingEntity != null) {
                session.merge(entity);
            }
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw new DatabaseException("Error updating entity in the database.", e);
        } finally {
            session.close();
        }
    }

    @Override
    public void delete(int id) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            T entity = session.get(entityType, id);
            if (entity != null) {
                session.delete(entity);
            }
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw new DatabaseException("Error deleting entity in the database.", e);
        } finally {
            session.close();
        }
    }

    @Override
    public List<T> getAll() {
        Session session = sessionFactory.openSession();
        try {
            String query = String.format("FROM %s", entityType.getSimpleName());
            return session.createQuery(query, entityType).getResultList();
        } catch (Exception e) {
            throw new DatabaseException("Error reading all the entities in the database.", e);
        } finally {
            session.close();
        }
    }
}