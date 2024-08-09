package erd.exmaple.erd.example.domain.service;

import erd.exmaple.erd.example.domain.ExhibitionEntity;
import erd.exmaple.erd.example.domain.Popup_StoreEntity;
import erd.exmaple.erd.example.domain.dto.ExhibitionDTO;
import erd.exmaple.erd.example.domain.dto.PopupStoreDTO;
import erd.exmaple.erd.example.domain.enums.District;
import erd.exmaple.erd.example.domain.enums.Region;
import erd.exmaple.erd.example.domain.repository.ExhibitionRepository;
import erd.exmaple.erd.example.domain.repository.PopupStoreRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RegionService {

    private final ExhibitionRepository exhibitionRepository;
    private final PopupStoreRepository popupStoreRepository;

    public RegionService(ExhibitionRepository exhibitionRepository, PopupStoreRepository popupStoreRepository) {
        this.exhibitionRepository = exhibitionRepository;
        this.popupStoreRepository = popupStoreRepository;
    }

    public List<District> getDistrictsByRegion(Region region) {
        return District.getDistrictsByRegion(region);
    }

    // 전체 전시회 목록
    public List<ExhibitionDTO> getAllExhibitions() {
        return exhibitionRepository.findAll().stream()
                .map(this::convertToExhibitionDTO)
                .collect(Collectors.toList());
    }

    // 전체 팝업스토어 목록
    public List<PopupStoreDTO> getAllPopupStores() {
        return popupStoreRepository.findAll().stream()
                .map(this::convertToPopupStoreDTO)
                .collect(Collectors.toList());
    }

    // 지역별 전시회 목록
    public List<ExhibitionDTO> getExhibitionsByRegion(Region region) {
        return exhibitionRepository.findByRegions(region).stream()
                .map(this::convertToExhibitionDTO)
                .collect(Collectors.toList());
    }

    // 지역별 팝업스토어 목록
    public List<PopupStoreDTO> getPopupStoresByRegion(Region region) {
        return popupStoreRepository.findByRegions(region).stream()
                .map(this::convertToPopupStoreDTO)
                .collect(Collectors.toList());
    }

    // 구별 전시회 목록
    public List<ExhibitionDTO> getExhibitionsByDistrict(String district) {
        District districtEnum = District.valueOf(district);
        return exhibitionRepository.findByDistrict(districtEnum).stream()
                .map(this::convertToExhibitionDTO)
                .collect(Collectors.toList());
    }

    // 구별 팝업스토어 목록
    public List<PopupStoreDTO> getPopupStoresByDistrict(String district) {
        District districtEnum = District.valueOf(district);
        return popupStoreRepository.findByDistrict(districtEnum).stream()
                .map(this::convertToPopupStoreDTO)
                .collect(Collectors.toList());
    }

    // 전체 전시회 인기순 목록
    public Page<ExhibitionDTO> getAllTopLikedExhibitions(Pageable pageable) {
        return exhibitionRepository.findAllByOrderByLikesCountDesc(pageable)
                .map(this::convertToExhibitionDTO);
    }

    // 전체 전시회 최신순 목록
    public Page<ExhibitionDTO> getAllLatestExhibitions(Pageable pageable) {
        return exhibitionRepository.findAllByOrderByCreatedAtDesc(pageable)
                .map(this::convertToExhibitionDTO);
    }

    // 전체 팝업스토어 인기순 목록
    public Page<PopupStoreDTO> getAllTopLikedPopupStores(Pageable pageable) {
        return popupStoreRepository.findAllByOrderByLikesCountDesc(pageable)
                .map(this::convertToPopupStoreDTO);
    }

    // 전체 팝업스토어 최신순 목록
    public Page<PopupStoreDTO> getAllLatestPopupStores(Pageable pageable) {
        return popupStoreRepository.findAllByOrderByCreatedAtDesc(pageable)
                .map(this::convertToPopupStoreDTO);
    }

    // 지역별 전시회 인기순 목록
    public Page<ExhibitionDTO> getTopLikedExhibitionsByRegion(Region region, Pageable pageable) {
        return exhibitionRepository.findAllByRegionsOrderByLikesCountDesc(region, pageable)
                .map(this::convertToExhibitionDTO);
    }

    // 지역별 전시회 최신순 목록
    public Page<ExhibitionDTO> getLatestExhibitionsByRegion(Region region, Pageable pageable) {
        return exhibitionRepository.findAllByRegionsOrderByCreatedAtDesc(region, pageable)
                .map(this::convertToExhibitionDTO);
    }

    // 지역별 팝업스토어 인기순 목록
    public Page<PopupStoreDTO> getTopLikedPopupStoresByRegion(Region region, Pageable pageable) {
        return popupStoreRepository.findAllByRegionsOrderByLikesCountDesc(region, pageable)
                .map(this::convertToPopupStoreDTO);
    }

    // 지역별 팝업스토어 최신순 목록
    public Page<PopupStoreDTO> getLatestPopupStoresByRegion(Region region, Pageable pageable) {
        return popupStoreRepository.findAllByRegionsOrderByCreatedAtDesc(region, pageable)
                .map(this::convertToPopupStoreDTO);
    }

    // 구별 전시회 인기순 목록
    public Page<ExhibitionDTO> getTopLikedExhibitionsByDistrict(District district, Pageable pageable) {
        return exhibitionRepository.findAllByDistrictOrderByLikesCountDesc(district, pageable)
                .map(this::convertToExhibitionDTO);
    }

    // 구별 전시회 최신순 목록
    public Page<ExhibitionDTO> getLatestExhibitionsByDistrict(District district, Pageable pageable) {
        return exhibitionRepository.findAllByDistrictOrderByCreatedAtDesc(district, pageable)
                .map(this::convertToExhibitionDTO);
    }

    // 구별 팝업스토어 인기순 목록
    public Page<PopupStoreDTO> getTopLikedPopupStoresByDistrict(District district, Pageable pageable) {
        return popupStoreRepository.findAllByDistrictOrderByLikesCountDesc(district, pageable)
                .map(this::convertToPopupStoreDTO);
    }

    // 구별 팝업스토어 최신순 목록
    public Page<PopupStoreDTO> getLatestPopupStoresByDistrict(District district, Pageable pageable) {
        return popupStoreRepository.findAllByDistrictOrderByCreatedAtDesc(district, pageable)
                .map(this::convertToPopupStoreDTO);
    }

    private ExhibitionDTO convertToExhibitionDTO(ExhibitionEntity entity) {
        return new ExhibitionDTO(
                entity.getId(),
                entity.getName(),
                entity.getPlace(), // 상세 위치
                entity.getDescription(),
                entity.getPhotoUrl(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.isHeart(),
                entity.getSearchDate(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getRegions().name(),
                entity.getDistrict().name() // District 추가

        );
    }

    private PopupStoreDTO convertToPopupStoreDTO(Popup_StoreEntity entity) {
        return new PopupStoreDTO(
                entity.getId(),
                entity.getName(),
                entity.getPlace(), // 상세 위치
                entity.getDescription(),
                entity.getPhotoUrl(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.isHeart(),
                entity.getSearchDate(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getRegions().name(),
                entity.getDistrict().name() // District 추가
        );
    }
}
