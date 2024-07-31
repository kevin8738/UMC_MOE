package erd.exmaple.erd.example.domain.service.RecordService;

import erd.exmaple.erd.example.domain.Record_PhotoBodyEntity;
import erd.exmaple.erd.example.domain.Record_PhotoEntity;
import erd.exmaple.erd.example.domain.converter.RecordCreationConverter;
import erd.exmaple.erd.example.domain.dto.RecordRequestDTO;
import erd.exmaple.erd.example.domain.dto.RecordResponseDTO;
import erd.exmaple.erd.example.domain.repository.RecordPhotoBodyRepository;
import erd.exmaple.erd.example.domain.repository.RecordPhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecordCreationServiceImpl implements RecordCreationService {
    private final RecordPhotoBodyRepository recordPhotoBodyRepository;
    private final RecordPhotoRepository recordPhotoRepository;

    @Override
    @Transactional
    public RecordResponseDTO.RecordResultDTO createOrUpdateRecord(Long recordPageId, Long recordPhotoId, RecordRequestDTO.RecordDTO request) {
        Record_PhotoEntity recordPhoto = recordPhotoRepository.findById(recordPhotoId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid recordPhotoId"));

        // record_photo_id로 기존 데이터 조회
        Optional<Record_PhotoBodyEntity> optionalRecordPhotoBody = recordPhotoBodyRepository.findByRecordPhoto(recordPhoto);

        Record_PhotoBodyEntity recordPhotoBody;
        if (optionalRecordPhotoBody.isPresent()) {
            // 기존 데이터가 존재하면 업데이트
            recordPhotoBody = optionalRecordPhotoBody.get();
            recordPhotoBody.setBody(request.getBody());
        } else {
            // 기존 데이터가 존재하지 않으면 새로운 데이터를 생성
            recordPhotoBody = RecordCreationConverter.toRecord_PhotoBodyEntity(request, recordPhoto);
        }

        Record_PhotoBodyEntity savedBody = recordPhotoBodyRepository.save(recordPhotoBody);
        return RecordCreationConverter.toRecordResultDTO(savedBody);
    }
}
