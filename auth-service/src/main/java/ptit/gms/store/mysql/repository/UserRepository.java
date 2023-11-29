package ptit.gms.store.mysql.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ptit.gms.dto.response.UserResDto;
import ptit.gms.store.mysql.entity.UserEntity;


@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
    boolean existsByEmailAndStatus(String email, int status);

    UserEntity findByEmailAndStatus(String email, int status);

    UserEntity findByIdAndStatus(String email, int status);

    @Query("SELECT NEW ptit.gms.dto.response.UserResDto(u.id, u.username, u.phone, u.email, r.roleName, r.roleType, u.createdDate, u.updatedDate)" +
            " FROM UserEntity u INNER JOIN RoleEntity r ON " +
            "u.roleType = r.roleType WHERE u.status = 1 AND u.username LIKE CONCAT('%', :username, '%') " +
            "AND u.email LIKE CONCAT('%', :email, '%')")
    Page<UserResDto> paginationUsers(String username, String email, Pageable pageable);

    @Query("SELECT NEW ptit.gms.dto.response.UserResDto(u.id, u.username, u.phone, u.email, r.roleName, r.roleType, u.createdDate, u.updatedDate) " +
            "FROM UserEntity u INNER JOIN RoleEntity r ON " +
            "u.roleType = r.roleType WHERE u.status = 1 AND u.id = :user_id")
    UserResDto getByUserId(@Param("user_id") String userId);
}
