package ua.agwebs.root.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.agwebs.root.entity.EntryHeader;

import java.time.LocalDate;
import java.util.List;

public interface EntryHeaderRepository extends JpaRepository<EntryHeader, Long>, JpaSpecificationExecutor<EntryHeader>{

    @Query("SELECT new ua.agwebs.root.repo.ShortBalanceLine(bk.id, acc.bsCategory, cur.code, SUM(ln.trnAmount)) " +
            "FROM ua.agwebs.root.entity.EntryHeader hdr " +
            "INNER JOIN hdr.lines ln " +
            "INNER JOIN hdr.book bk " +
            "INNER JOIN ln.account acc " +
            "INNER JOIN ln.currency cur " +
            "WHERE bk.id = :bookId " +
            "AND hdr.valueDate <= :reportDate " +
            "GROUP BY bk.id, acc.bsCategory, cur.code")
    public List<ShortBalanceLine> calcBookBalance(@Param("bookId") long bookId, @Param("reportDate") LocalDate reportDate);


}
