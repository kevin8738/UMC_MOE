package erd.exmaple.erd.example.domain.service;

import erd.exmaple.erd.example.domain.ExhibitionEntity;
import erd.exmaple.erd.example.domain.Popup_StoreEntity;
import erd.exmaple.erd.example.domain.enums.District;
import erd.exmaple.erd.example.domain.enums.Region;
import erd.exmaple.erd.example.domain.repository.ExhibitionRepository;
import erd.exmaple.erd.example.domain.repository.PopupStoreRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public List<ExhibitionEntity> getAllExhibitions() {
        return exhibitionRepository.findAll();
    }

    // 전체 팝업스토어 목록
    public List<Popup_StoreEntity> getAllPopupStores() {
        return popupStoreRepository.findAll();
    }

    // 지역별 전시회 목록
    public List<ExhibitionEntity> getExhibitionsByRegion(Region region) {
        return exhibitionRepository.findByRegions(region);
    }

    // 지역별 팝업스토어 목록
    public List<Popup_StoreEntity> getPopupStoresByRegion(Region region) {
        return popupStoreRepository.findByRegions(region);
    }

    // 구별 전시회 목록
    public List<ExhibitionEntity> getExhibitionsByDistrict(District district) {
        return exhibitionRepository.findByDistrict(district);
    }

    // 구별 팝업스토어 목록
    public List<Popup_StoreEntity> getPopupStoresByDistrict(District district) {
        return popupStoreRepository.findByDistrict(district);
    }

    // 전체 전시회 인기순 목록
    public Page<ExhibitionEntity> getAllTopLikedExhibitions(Pageable pageable) {
        return exhibitionRepository.findAllByOrderByLikesCountDesc(pageable);
    }

    // 전체 전시회 최신순 목록
    public Page<ExhibitionEntity> getAllLatestExhibitions(Pageable pageable) {
        return exhibitionRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    // 전체 팝업스토어 인기순 목록
    public Page<Popup_StoreEntity> getAllTopLikedPopupStores(Pageable pageable) {
        return popupStoreRepository.findAllByOrderByLikesCountDesc(pageable);
    }

    // 전체 팝업스토어 최신순 목록
    public Page<Popup_StoreEntity> getAllLatestPopupStores(Pageable pageable) {
        return popupStoreRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    // 지역별 전시회 인기순 목록
    public Page<ExhibitionEntity> getTopLikedExhibitionsByRegion(Region region, Pageable pageable) {
        return exhibitionRepository.findAllByRegionsOrderByLikesCountDesc(region, pageable);
    }

    // 지역별 전시회 최신순 목록
    public Page<ExhibitionEntity> getLatestExhibitionsByRegion(Region region, Pageable pageable) {
        return exhibitionRepository.findAllByRegionsOrderByCreatedAtDesc(region, pageable);
    }

    // 지역별 팝업스토어 인기순 목록
    public Page<Popup_StoreEntity> getTopLikedPopupStoresByRegion(Region region, Pageable pageable) {
        return popupStoreRepository.findAllByRegionsOrderByLikesCountDesc(region, pageable);
    }

    // 지역별 팝업스토어 최신순 목록
    public Page<Popup_StoreEntity> getLatestPopupStoresByRegion(Region region, Pageable pageable) {
        return popupStoreRepository.findAllByRegionsOrderByCreatedAtDesc(region, pageable);
    }

    // 구별 전시회 인기순 목록
    public Page<ExhibitionEntity> getTopLikedExhibitionsByDistrict(District district, Pageable pageable) {
        return exhibitionRepository.findAllByDistrictOrderByLikesCountDesc(district, pageable);
    }

    // 구별 전시회 최신순 목록
    public Page<ExhibitionEntity> getLatestExhibitionsByDistrict(District district, Pageable pageable) {
        return exhibitionRepository.findAllByDistrictOrderByCreatedAtDesc(district, pageable);
    }

    // 구별 팝업스토어 인기순 목록
    public Page<Popup_StoreEntity> getTopLikedPopupStoresByDistrict(District district, Pageable pageable) {
        return popupStoreRepository.findAllByDistrictOrderByLikesCountDesc(district, pageable);
    }

    // 구별 팝업스토어 최신순 목록
    public Page<Popup_StoreEntity> getLatestPopupStoresByDistrict(District district, Pageable pageable) {
        return popupStoreRepository.findAllByDistrictOrderByCreatedAtDesc(district, pageable);
    }
}

