package erd.exmaple.erd.example.domain.service.RecordService;

import erd.exmaple.erd.example.domain.Record_PageEntity;
import erd.exmaple.erd.example.domain.Record_PhotoEntity;
import erd.exmaple.erd.example.domain.converter.RecordPhotoConverter;
import erd.exmaple.erd.example.domain.dto.RecordPhotoRequestDTO;
import erd.exmaple.erd.example.domain.dto.RecordPhotoResponseDTO;
import erd.exmaple.erd.example.domain.enums.RecordStatus;
import erd.exmaple.erd.example.domain.repository.RecordPageRepository;
import erd.exmaple.erd.example.domain.repository.RecordPhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecordPhotoServiceImpl implements RecordPhotoService {
    private final RecordPhotoRepository recordPhotoRepository;
    private final RecordPageRepository recordPageRepository;

    @Override
    @Transactional
    public List<RecordPhotoResponseDTO.PhotoResponseDTO> receiveRecordPhoto(Long recordPageId, RecordPhotoRequestDTO.PhotoRequestDTO request) {
        // RecordPageEntity 가져오기
        Record_PageEntity recordPage = recordPageRepository.findById(recordPageId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid record page ID"));

        // 여러 개의 photoUrl 처리
        List<RecordPhotoResponseDTO.PhotoResponseDTO> responseList = new ArrayList<>();
        for (String photoUrl : request.getPhotoUrl()) {
            Record_PhotoEntity newPhoto = RecordPhotoConverter.toRecordPhotoEntity(photoUrl);
            newPhoto.setRecordPage(recordPage);
            Record_PhotoEntity savedPhoto = recordPhotoRepository.save(newPhoto);
            responseList.add(RecordPhotoConverter.toRecordPhotoResponseDTO(savedPhoto));
        }

        // RecordStatus 업데이트
        if (!recordPage.getRecordPhotoEntityList().isEmpty()) {
            recordPage.setStatus(RecordStatus.CERTIFIED);
            recordPageRepository.save(recordPage);
        }

        return responseList;
    }
}
