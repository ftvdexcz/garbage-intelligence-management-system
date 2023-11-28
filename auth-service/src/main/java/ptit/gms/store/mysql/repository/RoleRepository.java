package ptit.gms.store.mysql.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ptit.gms.dto.response.RoleResDto;
import ptit.gms.store.mysql.entity.RoleEntity;
import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, String> {
    @Query("SELECT NEW ptit.gms.dto.response.RoleResDto(r.code, r.roleName) FROM RoleEntity r")
    List<RoleResDto> listRoles();

    @Query("SELECT NEW ptit.gms.dto.response.RoleResDto(r.code, r.roleName) FROM RoleEntity r where r.code = :code")
    RoleResDto findByCode(@Param("code") String code);
}
