package erd.exmaple.erd.example.domain.service.RecordService;

import erd.exmaple.erd.example.domain.PhotoEntity;
import erd.exmaple.erd.example.domain.Record_PageEntity;
import erd.exmaple.erd.example.domain.Record_PhotoEntity;
import erd.exmaple.erd.example.domain.Record_PhotoBodyEntity;
import erd.exmaple.erd.example.domain.dto.ExhibitionOrPopupDetailsDTO;
import erd.exmaple.erd.example.domain.dto.RecordPageResponseDTO;
import erd.exmaple.erd.example.domain.dto.RecordPhotoDetailsDTO;
import erd.exmaple.erd.example.domain.repository.PhotoRepository;
import erd.exmaple.erd.example.domain.repository.RecordPageRepository;
import erd.exmaple.erd.example.domain.repository.RecordPhotoBodyRepository;
import erd.exmaple.erd.example.domain.repository.RecordPhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecordService {
    private final RecordPageRepository recordPageRepository;
    private final RecordPhotoRepository recordPhotoRepository;
    private final PhotoRepository photoRepository;
    private final RecordPhotoBodyRepository recordPhotoBodyRepository;

    public Record_PageEntity getRecordPageById(Long id, Long userId) {
        return recordPageRepository.findByIdAndUserId(id, userId).orElseThrow(() -> new RuntimeException("Record not found"));
    }

    public List<Record_PhotoEntity> getRecordPhotosByPageId(Long pageId, Long userId) {
        return recordPhotoRepository.findByRecordPage_IdAndRecordPage_User_Id(pageId, userId);
    }

    public Record_PhotoEntity getRecordPhotoById(Long photoId, Long userId) {
        return recordPhotoRepository.findByIdAndRecordPage_User_Id(photoId, userId).orElseThrow(() -> new RuntimeException("Photo not found"));
    }

    public PhotoEntity getPhotoByRecordPage(Record_PageEntity recordPage) {
        if (recordPage.getExhibition() != null) {
            return photoRepository.findByExhibition(recordPage.getExhibition()).orElse(null);
        } else if (recordPage.getPopupStore() != null) {
            return photoRepository.findByPopupStore(recordPage.getPopupStore()).orElse(null);
        }
        return null;
    }
    public Record_PhotoBodyEntity getRecordPhotoBodyByPhotoId(Long photoId) {
        return recordPhotoBodyRepository.findByRecordPhoto_Id(photoId).orElseThrow(() -> new RuntimeException("Photo body not found"));
    }
    public Page<Record_PageEntity> getPagedRecords(Pageable pageable, boolean isOldest, Long userId) {
        return isOldest
                ? recordPageRepository.findAllByUserIdOrderByCreatedAtAsc(pageable, userId)
                : recordPageRepository.findAllByUserIdOrderByCreatedAtDesc(pageable, userId);
    }

    public Page<RecordPageResponseDTO> getPagedRecordsDto(Pageable pageable, boolean isOldest, Long userId) {
        Page<Record_PageEntity> recordPages = getPagedRecords(pageable, isOldest, userId);
        List<RecordPageResponseDTO> recordResponseDtos = recordPages.getContent().stream().map(recordPage -> {
            PhotoEntity photoEntity = getPhotoByRecordPage(recordPage);
            String name = (recordPage.getExhibition() != null) ? recordPage.getExhibition().getName() : recordPage.getPopupStore().getName();
            String heart = recordPage.getExhibition() != null ?
                    recordPage.getExhibition().getFollowEntityList().stream()
                            .filter(follow -> follow.getUser().getId().equals(userId))
                            .findFirst()
                            .map(follow -> follow.getHeart() != null ? follow.getHeart().name() : "INACTIVE")
                            .orElse("ACTIVE")
                    : recordPage.getPopupStore().getFollowEntityList().stream()
                    .filter(follow -> follow.getUser().getId().equals(userId))
                    .findFirst()
                    .map(follow -> follow.getHeart() != null ? follow.getHeart().name() : "INACTIVE")
                    .orElse("ACTIVE");
            return RecordPageResponseDTO.builder()
                    .photo(photoEntity != null ? photoEntity.getPhoto() : null)
                    .name(name)
                    .startDate((recordPage.getExhibition() != null) ? recordPage.getExhibition().getStartDate().atStartOfDay() : recordPage.getPopupStore().getStartDate().atStartOfDay())
                    .endDate((recordPage.getExhibition() != null) ? recordPage.getExhibition().getEndDate().atStartOfDay() : recordPage.getPopupStore().getEndDate().atStartOfDay())
                    .heart(heart)
                    .build();
        }).collect(Collectors.toList());
        return new PageImpl<>(recordResponseDtos, pageable, recordPages.getTotalElements());
    }
    public ExhibitionOrPopupDetailsDTO getExhibitionOrPopupDetails(Long userId, Long id) {
        Record_PageEntity recordPage = getRecordPageById(id, userId);
        List<Record_PhotoEntity> recordPhotos = getRecordPhotosByPageId(id, userId);

        PhotoEntity photo = getPhotoByRecordPage(recordPage);

        List<String> recordPhotoUrls = recordPhotos.stream()
                .map(Record_PhotoEntity::getPhotoUrl)
                .collect(Collectors.toList());

        return ExhibitionOrPopupDetailsDTO.builder()
                .name(recordPage.getExhibition() != null ? recordPage.getExhibition().getName() : recordPage.getPopupStore().getName())
                .photo(photo != null ? photo.getPhoto() : null)
                .startDate((recordPage.getExhibition() != null) ? recordPage.getExhibition().getStartDate().atStartOfDay() : recordPage.getPopupStore().getStartDate().atStartOfDay())
                .endDate((recordPage.getExhibition() != null) ? recordPage.getExhibition().getEndDate().atStartOfDay() : recordPage.getPopupStore().getEndDate().atStartOfDay())
                .recordPhotos(recordPhotoUrls)
                .build();
    }
    public RecordPhotoDetailsDTO getRecordPhotoDetails(Long userId, Long photoId) {
        Record_PhotoEntity recordPhoto = getRecordPhotoById(photoId, userId);
        Record_PhotoBodyEntity recordPhotoBody = getRecordPhotoBodyByPhotoId(photoId);

        return RecordPhotoDetailsDTO.builder()
                .recordPhoto(recordPhoto.getPhotoUrl())
                .recordPhotoBody(recordPhotoBody.getBody())
                .build();
    }

}
