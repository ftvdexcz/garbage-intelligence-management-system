package ptit.gms.store.mysql.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ptit.gms.dto.response.TruckResDto;
import ptit.gms.store.mysql.entity.GarbageTruckEntity;

import java.util.List;

@Repository
public interface GarbageTruckRepository extends JpaRepository<GarbageTruckEntity, String> {
    @Query("SELECT new ptit.gms.dto.response.TruckResDto(g.plate, g.company, g.capacity, g.createdDate, g.updatedDate) " +
            "FROM GarbageTruckEntity g WHERE g.plate = :plate")
    TruckResDto getTruckByPlate(@Param("plate") String plate);

    @Query("SELECT new ptit.gms.dto.response.TruckResDto(g.plate, g.company, g.capacity, g.createdDate, g.updatedDate) FROM GarbageTruckEntity g")
    Page<TruckResDto> listBinsPagination(Pageable pageable);

    @Modifying
    int deleteByPlate(String plate);

    @Query("SELECT g.plate FROM GarbageTruckEntity g")
    List<String> getAllPlates();
}
