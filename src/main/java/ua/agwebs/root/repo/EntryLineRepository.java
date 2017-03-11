package ua.agwebs.root.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ua.agwebs.root.entity.EntryLine;
import ua.agwebs.root.entity.EntryLineId;

public interface EntryLineRepository extends JpaRepository<EntryLine, EntryLineId>, JpaSpecificationExecutor<EntryLine>{
}
