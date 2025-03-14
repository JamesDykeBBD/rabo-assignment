package nl.rabobank.transactionverifier.data.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * Abstract base class for Data Access Objects.
 * Provides common database operations and helper methods for entity management.
 *
 * @param <T> The entity type this DAO manages
 * @param <ID> The type of the entity's primary key
 */
@Transactional
public abstract class AbstractDAO<T, ID extends Serializable> {

    /**
     * Entity manager used for persistence operations.
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * The entity class this DAO manages.
     */
    private final Class<T> entityClass;

    /**
     * Constructs a new AbstractDAO with the specified entity class.
     *
     * @param entityClass The class of the entity this DAO manages
     */
    protected AbstractDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    /**
     * Gets the entity manager for use in subclasses.
     *
     * @return The entity manager
     */
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    /**
     * Gets the criteria builder for creating criteria queries.
     *
     * @return The criteria builder
     */
    protected CriteriaBuilder getCriteriaBuilder() {
        return entityManager.getCriteriaBuilder();
    }

    /**
     * Creates a criteria query for the entity type.
     *
     * @return A criteria query for the entity type
     */
    protected CriteriaQuery<T> createQuery() {
        return getCriteriaBuilder().createQuery(entityClass);
    }

    /**
     * Creates a criteria query for a specific return type.
     *
     * @param resultClass The class that will be returned by the query
     * @param <R> The type of the query result
     * @return A criteria query for the specified result type
     */
    protected <R> CriteriaQuery<R> createQuery(Class<R> resultClass) {
        return getCriteriaBuilder().createQuery(resultClass);
    }

    /**
     * Creates a root for the entity in a criteria query.
     *
     * @param query The criteria query
     * @return A root for the entity
     */
    protected Root<T> createRoot(CriteriaQuery<?> query) {
        return query.from(entityClass);
    }

    /**
     * Executes a typed query and returns the result list.
     *
     * @param typedQuery The typed query to execute
     * @return The list of results
     */
    protected List<T> getResultList(TypedQuery<T> typedQuery) {
        return typedQuery.getResultList();
    }

    /**
     * Executes a typed query and returns a single result as an Optional.
     *
     * @param typedQuery The typed query to execute
     * @return An Optional containing the single result, or empty if no result found
     */
    protected Optional<T> getSingleResult(TypedQuery<T> typedQuery) {
        try {
            return Optional.of(typedQuery.getSingleResult());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Saves an entity to the database.
     *
     * @param entity The entity to save
     * @return The saved entity
     */
    public T save(T entity) {
        entityManager.persist(entity);
        return entity;
    }

    /**
     * Updates an entity in the database.
     *
     * @param entity The entity to update
     * @return The updated entity
     */
    public T update(T entity) {
        return entityManager.merge(entity);
    }

    /**
     * Deletes an entity from the database.
     *
     * @param entity The entity to delete
     */
    public void delete(T entity) {
        entityManager.remove(entityManager.contains(entity) ?
                entity : entityManager.merge(entity));
    }

    /**
     * Finds an entity by its ID.
     *
     * @param id The ID of the entity to find
     * @return An Optional containing the found entity, or empty if not found
     */
    public Optional<T> findById(ID id) {
        CriteriaBuilder cb = getCriteriaBuilder();
        CriteriaQuery<T> cq = createQuery();
        Root<T> root = createRoot(cq);

        cq.select(root);
        cq.where(cb.equal(root.get("id"), id));

        return getSingleResult(entityManager.createQuery(cq));
    }

    /**
     * Finds all entities of the managed type.
     *
     * @return A list of all entities
     */
    public List<T> findAll() {
        CriteriaQuery<T> cq = createQuery();
        Root<T> root = createRoot(cq);
        cq.select(root);

        return getResultList(entityManager.createQuery(cq));
    }

    /**
     * Counts the total number of entities of the managed type.
     *
     * @return The total count of entities
     */
    public Long count() {
        CriteriaBuilder cb = getCriteriaBuilder();
        CriteriaQuery<Long> cq = createQuery(Long.class);
        Root<T> root = createRoot(cq);

        cq.select(cb.count(root));

        return entityManager.createQuery(cq).getSingleResult();
    }

    /**
     * Finds all entities with pagination.
     *
     * @param pageNumber The page number (1-based indexing)
     * @param pageSize The number of records per page
     * @return A list of entities for the specified page
     */
    public List<T> findAllPaginated(int pageNumber, int pageSize) {
        CriteriaQuery<T> cq = createQuery();
        Root<T> root = createRoot(cq);
        cq.select(root);

        TypedQuery<T> query = entityManager.createQuery(cq);
        query.setFirstResult((pageNumber - 1) * pageSize);
        query.setMaxResults(pageSize);

        return getResultList(query);
    }
}