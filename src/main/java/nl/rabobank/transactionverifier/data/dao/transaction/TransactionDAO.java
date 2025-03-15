package nl.rabobank.transactionverifier.data.dao.transaction;

import nl.rabobank.transactionverifier.data.dao.AbstractDAO;
import nl.rabobank.transactionverifier.data.entity.TransactionEntity;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for Transaction entities.
 * Provides methods to create, read, update, and delete transaction records in the database.
 * Extends the AbstractDAO to leverage common database operations.
 */
@Repository
public class TransactionDAO extends AbstractDAO<TransactionEntity, Long> {

    /**
     * Constructs a new TransactionDAO.
     */
    public TransactionDAO() {
        super(TransactionEntity.class);
    }

    /**
     * Finds a transaction by its reference number.
     * Reference numbers should be unique across all transactions.
     *
     * @param reference The reference number to search for
     * @return An Optional containing the found transaction, or empty if not found
     */
    public Optional<TransactionEntity> findByReference(String reference) {
        CriteriaQuery<TransactionEntity> cq = createQuery();
        Root<TransactionEntity> root = createRoot(cq);

        cq.select(root);
        cq.where(getCriteriaBuilder().equal(root.get("reference"), reference));

        TypedQuery<TransactionEntity> query = getEntityManager().createQuery(cq);
        return getSingleResult(query);
    }

    /**
     * Finds all transactions associated with a specific account number.
     *
     * @param accountNumber The account number to search for
     * @return A list of transactions for the specified account
     */
    public List<TransactionEntity> findByAccountNumber(String accountNumber) {
        CriteriaQuery<TransactionEntity> cq = createQuery();
        Root<TransactionEntity> root = createRoot(cq);

        cq.select(root);
        cq.where(getCriteriaBuilder().equal(root.get("accountNumber"), accountNumber));

        TypedQuery<TransactionEntity> query = getEntityManager().createQuery(cq);
        return getResultList(query);
    }

    /**
     * Finds transactions with mutation values greater than the specified amount.
     * Useful for identifying transactions above a certain threshold.
     *
     * @param amount The threshold amount
     * @return A list of transactions with mutations greater than the specified amount
     */
    public List<TransactionEntity> findByMutationGreaterThan(BigDecimal amount) {
        CriteriaQuery<TransactionEntity> cq = createQuery();
        Root<TransactionEntity> root = createRoot(cq);

        cq.select(root);
        cq.where(getCriteriaBuilder().greaterThan(root.get("mutation"), amount));

        TypedQuery<TransactionEntity> query = getEntityManager().createQuery(cq);
        return getResultList(query);
    }

    /**
     * Deletes a transaction by its ID.
     *
     * @param id The ID of the transaction to delete
     * @return true if the transaction was found and deleted, false otherwise
     */
    public boolean deleteById(Long id) {
        Optional<TransactionEntity> transaction = findById(id);
        if (transaction.isPresent()) {
            delete(transaction.get());
            return true;
        }
        return false;
    }

    /**
     * Performs an advanced search for transactions based on multiple optional criteria.
     * Any parameter can be null to indicate that criterion should not be used in the search.
     *
     * @param reference The reference number (partial match)
     * @param accountNumber The account number (exact match)
     * @param minAmount The minimum mutation amount (inclusive)
     * @param maxAmount The maximum mutation amount (inclusive)
     * @return A list of transactions matching all the provided criteria
     */
    public List<TransactionEntity> search(String reference, String accountNumber,
                                          BigDecimal minAmount, BigDecimal maxAmount) {
        CriteriaQuery<TransactionEntity> cq = createQuery();
        Root<TransactionEntity> root = createRoot(cq);

        Predicate predicate = getCriteriaBuilder().conjunction();

        if (reference != null && !reference.isEmpty()) {
            predicate = getCriteriaBuilder().and(predicate,
                    getCriteriaBuilder().like(root.get("reference"), "%" + reference + "%"));
        }

        if (accountNumber != null && !accountNumber.isEmpty()) {
            predicate = getCriteriaBuilder().and(predicate,
                    getCriteriaBuilder().equal(root.get("accountNumber"), accountNumber));
        }

        if (minAmount != null) {
            predicate = getCriteriaBuilder().and(predicate,
                    getCriteriaBuilder().greaterThanOrEqualTo(root.get("mutation"), minAmount));
        }

        if (maxAmount != null) {
            predicate = getCriteriaBuilder().and(predicate,
                    getCriteriaBuilder().lessThanOrEqualTo(root.get("mutation"), maxAmount));
        }

        cq.select(root).where(predicate);
        return getResultList(getEntityManager().createQuery(cq));
    }

    /**
     * Finds all transactions for a specific report.
     *
     * @param reportId
     * @return
     */
    public List<TransactionEntity> findAllForReport(long reportId) {
        CriteriaQuery<TransactionEntity> cq = createQuery();
        Root<TransactionEntity> root = createRoot(cq);

        cq.select(root);
        cq.where(getCriteriaBuilder().equal(root.get("reportId"), reportId));

        TypedQuery<TransactionEntity> query = getEntityManager().createQuery(cq);
        return getResultList(query);
    }
}