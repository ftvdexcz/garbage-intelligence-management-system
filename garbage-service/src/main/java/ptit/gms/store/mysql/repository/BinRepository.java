package ptit.gms.store.mysql.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ptit.gms.dto.response.GetBinResDto;
import ptit.gms.store.mysql.entity.BinEntity;
import java.util.List;

@Repository
public interface BinRepository extends JpaRepository<BinEntity, String> {

    @Query("SELECT new ptit.gms.dto.response.GetBinResDto(b.id," +
            " b.company," +
            " b.address," +
            " b.lat," +
            " b.lon," +
            " c.weight," +
            " c.updatedTimestamp," +
            " b.capacity," +
            " b.createdDate," +
            " b.updatedDate," +
            " b.createdUser," +
            " b.imageUrl," +
            " o.id," +
            " o.name," +
            " o.phone," +
            " o.email) " +
            "FROM BinEntity b INNER JOIN CompanyOwnerEntity o " +
            "ON b.ownerId = o.id INNER JOIN TotalCellEntity c ON c.binCode = b.id " +
            "WHERE b.id = :id AND b.status = 1")
    GetBinResDto getBinById(@Param("id") String id);

    @Query("SELECT new ptit.gms.dto.response.GetBinResDto(b.id," +
            " b.company," +
            " b.address," +
            " b.lat," +
            " b.lon," +
            " c.weight," +
            " c.updatedTimestamp," +
            " b.capacity," +
            " b.createdDate," +
            " b.updatedDate," +
            " b.createdUser," +
            " b.imageUrl," +
            " o.id," +
            " o.name," +
            " o.phone," +
            " o.email) " +
            "FROM BinEntity b INNER JOIN CompanyOwnerEntity o " +
            "ON b.ownerId = o.id INNER JOIN TotalCellEntity c ON c.binCode = b.id WHERE b.status = 1")
    List<GetBinResDto> listBins();

    @Query("SELECT new ptit.gms.dto.response.GetBinResDto(b.id," +
            " b.company," +
            " b.address," +
            " b.lat," +
            " b.lon," +
            " c.weight," +
            " c.updatedTimestamp," +
            " b.capacity," +
            " b.createdDate," +
            " b.updatedDate," +
            " b.createdUser," +
            " b.imageUrl," +
            " o.id," +
            " o.name," +
            " o.phone," +
            " o.email) " +
            "FROM BinEntity b INNER JOIN CompanyOwnerEntity o " +
            "ON b.ownerId = o.id INNER JOIN TotalCellEntity c ON c.binCode = b.id " +
            "WHERE b.createdUser = :created_user AND b.status = 1")
    List<GetBinResDto> listBinsByCreatedUser(@Param("created_user") String createdUser);
}
