package erd.exmaple.erd.example.domain.service.SearchService;

import erd.exmaple.erd.example.domain.*;
import erd.exmaple.erd.example.domain.dto.SearchResultDTO;
import erd.exmaple.erd.example.domain.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SearchPageService {
    private final ExhibitionRepository exhibitionRepository;
    private final PopupStoreRepository popupStoreRepository;
    private final FollowRepository followRepository;
    private final PhotoRepository photoRepository;
    private final UserRepository userRepository;
    private final SearchRepository searchRepository;

    @Transactional
    public List<SearchResultDTO> search(String keyword, Long userId, PageRequest pageable) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        List<SearchResultDTO> results = new ArrayList<>();

        // Exhibition 검색 및 결과 추가
        List<ExhibitionEntity> exhibitions = exhibitionRepository.findByNameContaining(keyword);
        for (ExhibitionEntity exhibition : exhibitions) {
            updateOrCreateSearchEntity(user, exhibition, null);

            PhotoEntity photo = photoRepository.findByExhibition(exhibition).orElse(null);
            boolean followed = followRepository.findByUserAndExhibition(user, exhibition).isPresent();
            results.add(toSearchResultDTO(exhibition, photo, followed));
        }

        // PopupStore 검색 및 결과 추가
        List<Popup_StoreEntity> popupStores = popupStoreRepository.findByNameContaining(keyword);
        for (Popup_StoreEntity popupStore : popupStores) {
            updateOrCreateSearchEntity(user, null, popupStore);

            PhotoEntity photo = photoRepository.findByPopupStore(popupStore).orElse(null);
            boolean followed = followRepository.findByUserAndPopupStore(user, popupStore).isPresent();
            results.add(toSearchResultDTO(popupStore, photo, followed));
        }

        // 페이징 처리
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), results.size());
        List<SearchResultDTO> pagedResults = results.subList(start, end);

        return pagedResults;
    }

    @Transactional
    public List<SearchResultDTO> getRecentSearches(Long userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        List<SearchEntity> recentSearchEntities = searchRepository.findTop4ByUserIdOrderBySearchDateDesc(userId);
        List<SearchResultDTO> results = new ArrayList<>();

        for (SearchEntity searchEntity : recentSearchEntities) {
            PhotoEntity photo = null;
            boolean followed = false;

            if (searchEntity.getExhibition() != null) {
                photo = photoRepository.findByExhibition(searchEntity.getExhibition()).orElse(null);
                followed = followRepository.findByUserAndExhibition(user, searchEntity.getExhibition()).isPresent();
                results.add(toSearchResultDTO(searchEntity.getExhibition(), photo, followed));
            }

            if (searchEntity.getPopupStore() != null) {
                photo = photoRepository.findByPopupStore(searchEntity.getPopupStore()).orElse(null);
                followed = followRepository.findByUserAndPopupStore(user, searchEntity.getPopupStore()).isPresent();
                results.add(toSearchResultDTO(searchEntity.getPopupStore(), photo, followed));
            }
        }

        return results;
    }

    private void updateOrCreateSearchEntity(UserEntity user, ExhibitionEntity exhibition, Popup_StoreEntity popupStore) {
        Optional<SearchEntity> existingSearch = Optional.empty();
        if (exhibition != null) {
            existingSearch = searchRepository.findByUserAndExhibition(user, exhibition);
        } else if (popupStore != null) {
            existingSearch = searchRepository.findByUserAndPopupStore(user, popupStore);
        }

        if (existingSearch.isPresent()) {
            SearchEntity searchEntity = existingSearch.get();
            searchEntity.setSearchDate(LocalDateTime.now());
            searchRepository.save(searchEntity);
        } else {
            SearchEntity searchEntity = new SearchEntity(null, user, exhibition, popupStore, LocalDateTime.now());
            searchRepository.save(searchEntity);
        }
    }

    private SearchResultDTO toSearchResultDTO(ExhibitionEntity exhibition, PhotoEntity photo, boolean followed) {
        return SearchResultDTO.builder()
                .id(exhibition.getId())
                .name(exhibition.getName())
                .photo(photo != null ? photo.getPhoto() : null)
                .startDate(exhibition.getStartDate().atStartOfDay())
                .endDate(exhibition.getEndDate().atStartOfDay())
                .followed(followed)
                .build();
    }

    private SearchResultDTO toSearchResultDTO(Popup_StoreEntity popupStore, PhotoEntity photo, boolean followed) {
        return SearchResultDTO.builder()
                .id(popupStore.getId())
                .name(popupStore.getName())
                .photo(photo != null ? photo.getPhoto() : null)
                .startDate(popupStore.getStartDate().atStartOfDay())
                .endDate(popupStore.getEndDate().atStartOfDay())
                .followed(followed)
                .build();
    }
}
