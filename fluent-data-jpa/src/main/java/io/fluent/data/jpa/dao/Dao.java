package io.fluent.data.jpa.dao;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.stereotype.Repository;

@Repository
public class Dao<T, ID extends Serializable> implements GenericDao<T, ID> {

    private static final Logger logger = Logger.getLogger(Dao.class.getName());

    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private int batchSize;

    private final EntityManagerFactory entityManagerFactory;
    private final JpaContext jpaContext;
    private JpaEntityInformation jpaEntityInformation;
    public Dao(EntityManagerFactory entityManagerFactory, JpaContext jpaContext) {
        this.entityManagerFactory = entityManagerFactory;
        this.jpaContext = jpaContext;
    }
    protected EntityManager getEntityManager(T entity) {
        return jpaContext.getEntityManagerByManagedType(entity.getClass());
    }
    @Override
    public <S extends T> void saveInBatch(Iterable<S> entities) {

        if (entities == null) {
            throw new IllegalArgumentException("The given Iterable of entities not be null!");
        }

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        Session session = entityManager.unwrap(Session.class);
        session.setJdbcBatchSize(10);
        try {
            entityTransaction.begin();

            int i = 0;
            for (S entity: entities) {
                if (i % batchSize == 0 && i > 0) {
                    logger.log(Level.INFO,
                            "Flushing the EntityManager containing {0} entities ...", batchSize);

                    entityTransaction.commit();
                    entityTransaction.begin();

                    entityManager.clear();
                }

                entityManager.persist(entity);
                i++;
            }

            logger.log(Level.INFO,
                    "Flushing the remaining entities ...");

            entityTransaction.commit();
        } catch (RuntimeException e) {
            if (entityTransaction.isActive()) {
                entityTransaction.rollback();
            }

            throw e;
        } finally {
            entityManager.close();
        }
    }
}
