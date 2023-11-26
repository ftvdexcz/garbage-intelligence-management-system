package ptit.gms.store.mysql.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptit.gms.store.mysql.entity.TotalCellEntity;

@Repository
public interface TotalCellRepository extends JpaRepository<TotalCellEntity, Long> {
    TotalCellEntity findByBinCode(String binCode);

}
