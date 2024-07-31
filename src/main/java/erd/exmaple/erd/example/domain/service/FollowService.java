package erd.exmaple.erd.example.domain.service;

import erd.exmaple.erd.example.domain.ExhibitionEntity;
import erd.exmaple.erd.example.domain.FollowEntity;
import erd.exmaple.erd.example.domain.Popup_StoreEntity;
import erd.exmaple.erd.example.domain.UserEntity;
import erd.exmaple.erd.example.domain.dto.ExhibitionDTO;
import erd.exmaple.erd.example.domain.dto.FollowDTO;
import erd.exmaple.erd.example.domain.dto.PopupStoreDTO;
import erd.exmaple.erd.example.domain.repository.ExhibitionRepository;
import erd.exmaple.erd.example.domain.repository.FollowRepository;
import erd.exmaple.erd.example.domain.repository.PopupStoreRepository;
import erd.exmaple.erd.example.domain.repository.UserRepository;
import erd.exmaple.erd.example.domain.enums.Heart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FollowService {

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExhibitionRepository exhibitionRepository;

    @Autowired
    private PopupStoreRepository popupStoreRepository;

    @Transactional(readOnly = true)
    public Page<FollowDTO> getUserFollowsByLatest(Long userId, Pageable pageable) {
        return followRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable)
                .map(this::convertToDTO);
    }

    @Transactional(readOnly = true)
    public Page<FollowDTO> getUserFollowsByOldest(Long userId, Pageable pageable) {
        return followRepository.findByUserIdOrderByCreatedAtAsc(userId, pageable)
                .map(this::convertToDTO);
    }

    @Transactional
    public FollowDTO followExhibition(Long userId, Long exhibitionId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        ExhibitionEntity exhibition = exhibitionRepository.findById(exhibitionId)
                .orElseThrow(() -> new RuntimeException("Exhibition not found with ID: " + exhibitionId));

        // 좋아요 수 증가
        exhibition.incrementLikesCount();
        exhibitionRepository.save(exhibition);


        FollowEntity follow = FollowEntity.builder()
                .user(user)
                .exhibition(exhibition)
                .heart(Heart.ACTIVE)
                .build();

        return convertToDTO(followRepository.save(follow));
    }

    @Transactional
    public FollowDTO followPopupStore(Long userId, Long popupStoreId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        Popup_StoreEntity popupStore = popupStoreRepository.findById(popupStoreId)
                .orElseThrow(() -> new RuntimeException("Popup store not found with ID: " + popupStoreId));
        // 좋아요 수 증가
        popupStore.incrementLikesCount();
        popupStoreRepository.save(popupStore);

        FollowEntity follow = FollowEntity.builder()
                .user(user)
                .popupStore(popupStore)
                .heart(Heart.ACTIVE)
                .build();

        return convertToDTO(followRepository.save(follow));
    }

    @Transactional
    public void unfollow(Long followId) {
        followRepository.deleteById(followId);
    }

    @Transactional
    public FollowDTO toggleHeart(Long followId) {
        FollowEntity follow = followRepository.findById(followId)
                .orElseThrow(() -> new RuntimeException("Follow not found"));
        follow.setHeart(follow.getHeart() == Heart.ACTIVE ? Heart.INACTIVE : Heart.ACTIVE);
        return convertToDTO(followRepository.save(follow));
    }

    private FollowDTO convertToDTO(FollowEntity followEntity) {
        ExhibitionDTO exhibitionDTO = followEntity.getExhibition() != null ? convertToExhibitionDTO(followEntity.getExhibition()) : null;
        PopupStoreDTO popupStoreDTO = followEntity.getPopupStore() != null ? convertToPopupStoreDTO(followEntity.getPopupStore()) : null;

        return FollowDTO.builder()
                .id(followEntity.getId())
                .exhibition(exhibitionDTO)
                .popupStore(popupStoreDTO)
                .heart(followEntity.getHeart())
                .createdAt(followEntity.getCreatedAt())
                .updatedAt(followEntity.getUpdatedAt())
                .build();
    }

    private ExhibitionDTO convertToExhibitionDTO(ExhibitionEntity exhibitionEntity) {
        if (exhibitionEntity == null) {
            return null;
        }
        return ExhibitionDTO.builder()
                .id(exhibitionEntity.getId())
                .name(exhibitionEntity.getName())
                .place(exhibitionEntity.getPlace())
                .description(exhibitionEntity.getDescription())
                .startDate(exhibitionEntity.getStartDate())
                .endDate(exhibitionEntity.getEndDate())
                .searchDate(exhibitionEntity.getSearchDate())
                .build();
    }

    private PopupStoreDTO convertToPopupStoreDTO(Popup_StoreEntity popupStoreEntity) {
        if (popupStoreEntity == null) {
            return null;
        }
        return PopupStoreDTO.builder()
                .id(popupStoreEntity.getId())
                .name(popupStoreEntity.getName())
                .place(popupStoreEntity.getPlace())
                .description(popupStoreEntity.getDescription())
                .startDate(popupStoreEntity.getStartDate())
                .endDate(popupStoreEntity.getEndDate())
                .searchDate(popupStoreEntity.getSearchDate())
                .build();
    }
}