package erd.exmaple.erd.example.domain.service.RecordService;

import erd.exmaple.erd.example.domain.dto.RecordRequestDTO;
import erd.exmaple.erd.example.domain.dto.RecordResponseDTO;

public interface RecordCreationService {
    RecordResponseDTO.RecordResultDTO createOrUpdateRecord(Long recordPageId, Long recordPhotoId, RecordRequestDTO.RecordDTO request);
}
