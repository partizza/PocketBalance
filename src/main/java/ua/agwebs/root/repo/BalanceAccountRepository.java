package ua.agwebs.root.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ua.agwebs.root.entity.BalanceAccount;
import ua.agwebs.root.entity.BalanceAccountId;

public interface BalanceAccountRepository extends JpaRepository<BalanceAccount, BalanceAccountId>, JpaSpecificationExecutor<BalanceAccount> {
}
