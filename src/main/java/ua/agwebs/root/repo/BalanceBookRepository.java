package ua.agwebs.root.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ua.agwebs.root.entity.BalanceBook;

public interface BalanceBookRepository extends JpaRepository<BalanceBook, Long>, JpaSpecificationExecutor<BalanceBook> {
}
