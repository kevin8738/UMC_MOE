package erd.exmaple.erd.example.domain.repository;

import erd.exmaple.erd.example.domain.ExhibitionEntity;
import erd.exmaple.erd.example.domain.enums.District;
import erd.exmaple.erd.example.domain.enums.Region;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExhibitionRepository extends JpaRepository<ExhibitionEntity, Long> {
    List<ExhibitionEntity> findByNameContaining(String keyword);
    Page<ExhibitionEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);
    Page<ExhibitionEntity> findAllByOrderByLikesCountDesc(Pageable pageable);
    List<ExhibitionEntity> findByRegions(Region region);
    List<ExhibitionEntity> findByDistrict(District district);

    @Query("SELECT e FROM ExhibitionEntity e WHERE e.regions = :region ORDER BY e.likesCount DESC")
    Page<ExhibitionEntity> findAllByRegionsOrderByLikesCountDesc(@Param("region") Region region, Pageable pageable);

    @Query("SELECT e FROM ExhibitionEntity e WHERE e.regions = :region ORDER BY e.createdAt DESC")
    Page<ExhibitionEntity> findAllByRegionsOrderByCreatedAtDesc(@Param("region") Region region, Pageable pageable);

    @Query("SELECT e FROM ExhibitionEntity e WHERE e.district = :district ORDER BY e.likesCount DESC")
    Page<ExhibitionEntity> findAllByDistrictOrderByLikesCountDesc(@Param("district") District district, Pageable pageable);

    @Query("SELECT e FROM ExhibitionEntity e WHERE e.district = :district ORDER BY e.createdAt DESC")
    Page<ExhibitionEntity> findAllByDistrictOrderByCreatedAtDesc(@Param("district") District district, Pageable pageable);
}
