package ua.agwebs.root.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ua.agwebs.root.entity.TransactionDetail;
import ua.agwebs.root.entity.TransactionDetailId;

public interface TransactionDetailRepository extends JpaRepository<TransactionDetail, TransactionDetailId>, JpaSpecificationExecutor<TransactionDetail> {
}
