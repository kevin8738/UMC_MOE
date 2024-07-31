package erd.exmaple.erd.example.domain.repository;

import erd.exmaple.erd.example.domain.ExhibitionEntity;
import erd.exmaple.erd.example.domain.FollowEntity;
import erd.exmaple.erd.example.domain.Popup_StoreEntity;
import erd.exmaple.erd.example.domain.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<FollowEntity, Long> {
    Optional<FollowEntity> findByUserAndExhibition(UserEntity user, ExhibitionEntity exhibition);
    Optional<FollowEntity> findByUserAndPopupStore(UserEntity user, Popup_StoreEntity popupStore);
    Page<FollowEntity> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    Page<FollowEntity> findByUserIdOrderByCreatedAtAsc(Long userId, Pageable pageable);

}
