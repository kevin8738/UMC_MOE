package erd.exmaple.erd.example.domain.repository;

import erd.exmaple.erd.example.domain.ExhibitionEntity;
import erd.exmaple.erd.example.domain.PhotoEntity;
import erd.exmaple.erd.example.domain.Popup_StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PhotoRepository extends JpaRepository<PhotoEntity, Long> {
    Optional<PhotoEntity> findByExhibition(ExhibitionEntity exhibition);
    Optional<PhotoEntity> findByPopupStore(Popup_StoreEntity popupStore);
}
