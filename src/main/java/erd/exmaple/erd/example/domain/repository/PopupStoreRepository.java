package erd.exmaple.erd.example.domain.repository;

import erd.exmaple.erd.example.domain.Popup_StoreEntity;
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
public interface PopupStoreRepository extends JpaRepository<Popup_StoreEntity, Long> {
    List<Popup_StoreEntity> findByNameContaining(String keyword);
    Page<Popup_StoreEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);
    Page<Popup_StoreEntity> findAllByOrderByLikesCountDesc(Pageable pageable);
    List<Popup_StoreEntity> findByRegions(Region region);
    List<Popup_StoreEntity> findByDistrict(District district);

    @Query("SELECT p FROM Popup_StoreEntity p WHERE p.regions = :region ORDER BY p.likesCount DESC")
    Page<Popup_StoreEntity> findAllByRegionsOrderByLikesCountDesc(@Param("region") Region region, Pageable pageable);

    @Query("SELECT p FROM Popup_StoreEntity p WHERE p.regions = :region ORDER BY p.createdAt DESC")
    Page<Popup_StoreEntity> findAllByRegionsOrderByCreatedAtDesc(@Param("region") Region region, Pageable pageable);

    @Query("SELECT p FROM Popup_StoreEntity p WHERE p.district = :district ORDER BY p.likesCount DESC")
    Page<Popup_StoreEntity> findAllByDistrictOrderByLikesCountDesc(@Param("district") District district, Pageable pageable);

    @Query("SELECT p FROM Popup_StoreEntity p WHERE p.district = :district ORDER BY p.createdAt DESC")
    Page<Popup_StoreEntity> findAllByDistrictOrderByCreatedAtDesc(@Param("district") District district, Pageable pageable);
}
