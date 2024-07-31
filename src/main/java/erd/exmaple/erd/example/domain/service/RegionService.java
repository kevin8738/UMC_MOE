package erd.exmaple.erd.example.domain.service;

import erd.exmaple.erd.example.domain.ExhibitionEntity;
import erd.exmaple.erd.example.domain.Popup_StoreEntity;
import erd.exmaple.erd.example.domain.dto.ExhibitionDTO;
import erd.exmaple.erd.example.domain.dto.PopupStoreDTO;
import erd.exmaple.erd.example.domain.enums.District;
import erd.exmaple.erd.example.domain.enums.Region;
import erd.exmaple.erd.example.domain.repository.ExhibitionRepository;
import erd.exmaple.erd.example.domain.repository.PopupStoreRepository;
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

    public List<ExhibitionDTO> getAllExhibitions() {
        return exhibitionRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<PopupStoreDTO> getAllPopupStores() {
        return popupStoreRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ExhibitionDTO> getExhibitionsByRegion(Region regions) {
        return exhibitionRepository.findByRegions(regions).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ExhibitionDTO> getExhibitionsByDistrict(String district) {
        District districtEnum = District.valueOf(district);
        return exhibitionRepository.findByDistrict(districtEnum).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<PopupStoreDTO> getPopupStoresByRegion(Region regions) {
        return popupStoreRepository.findByRegions(regions).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<PopupStoreDTO> getPopupStoresByDistrict(String district) {
        District districtEnum = District.valueOf(district);
        return popupStoreRepository.findByDistrict(districtEnum).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private ExhibitionDTO convertToDTO(ExhibitionEntity entity) {
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

    private PopupStoreDTO convertToDTO(Popup_StoreEntity entity) {
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
