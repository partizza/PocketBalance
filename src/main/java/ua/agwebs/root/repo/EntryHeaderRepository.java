package ua.agwebs.root.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ua.agwebs.root.entity.EntryHeader;

public interface EntryHeaderRepository extends JpaRepository<EntryHeader, Long>, JpaSpecificationExecutor<EntryHeader>{
}
