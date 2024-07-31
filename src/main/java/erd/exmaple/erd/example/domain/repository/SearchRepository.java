package erd.exmaple.erd.example.domain.repository;

import erd.exmaple.erd.example.domain.SearchEntity;
import erd.exmaple.erd.example.domain.UserEntity;
import erd.exmaple.erd.example.domain.ExhibitionEntity;
import erd.exmaple.erd.example.domain.Popup_StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SearchRepository extends JpaRepository<SearchEntity, Long> {
    List<SearchEntity> findTop4ByUserIdOrderBySearchDateDesc(Long userId);
    Optional<SearchEntity> findByUserAndExhibition(UserEntity user, ExhibitionEntity exhibition);
    Optional<SearchEntity> findByUserAndPopupStore(UserEntity user, Popup_StoreEntity popupStore);
}
