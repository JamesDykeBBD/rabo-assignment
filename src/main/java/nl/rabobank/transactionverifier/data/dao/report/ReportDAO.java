package nl.rabobank.transactionverifier.data.dao.report;

import aj.org.objectweb.asm.commons.Remapper;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import nl.rabobank.transactionverifier.data.dao.AbstractDAO;
import nl.rabobank.transactionverifier.data.entity.ReportEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ReportDAO extends AbstractDAO<ReportEntity, Long> {

    public ReportDAO() {
        super(ReportEntity.class);
    }

    public Optional<ReportEntity> findByReportId(String fileId) {
        CriteriaBuilder cb = getCriteriaBuilder();
        CriteriaQuery<ReportEntity> cq = createQuery();
        Root<ReportEntity> root = createRoot(cq);

        cq.select(root);
        cq.where(cb.equal(root.get("fileId"), fileId));

        TypedQuery<ReportEntity> query = getEntityManager().createQuery(cq);
        return getSingleResult(query);
    }
}
