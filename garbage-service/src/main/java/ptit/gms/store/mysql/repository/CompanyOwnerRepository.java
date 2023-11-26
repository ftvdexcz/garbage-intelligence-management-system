package ptit.gms.store.mysql.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptit.gms.store.mysql.entity.CompanyOwnerEntity;

@Repository
public interface CompanyOwnerRepository extends JpaRepository<CompanyOwnerEntity, String> {

}
