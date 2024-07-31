package erd.exmaple.erd.example.domain.service.RecordService;

import erd.exmaple.erd.example.domain.dto.RecordPhotoRequestDTO;
import erd.exmaple.erd.example.domain.dto.RecordPhotoResponseDTO;

import java.util.List;

public interface RecordPhotoService {
    List<RecordPhotoResponseDTO.PhotoResponseDTO> receiveRecordPhoto(Long recordPageId, RecordPhotoRequestDTO.PhotoRequestDTO request);
}
