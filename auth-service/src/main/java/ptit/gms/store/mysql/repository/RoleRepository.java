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

    boolean existsByRoleType(int roleType);
    @Query("SELECT NEW ptit.gms.dto.response.RoleResDto(r.roleName, r.roleType) FROM RoleEntity r")
    List<RoleResDto> listRoles();

    @Query("SELECT NEW ptit.gms.dto.response.RoleResDto(r.roleName, r.roleType) FROM RoleEntity r where r.roleType = :type")
    RoleResDto findByType(@Param("type") int roleType);
}
