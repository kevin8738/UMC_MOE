package erd.exmaple.erd.example.domain.controller;

import erd.exmaple.erd.example.domain.dto.RecordRequestDTO;
import erd.exmaple.erd.example.domain.dto.RecordResponseDTO;
import erd.exmaple.erd.example.domain.service.RecordService.RecordCreationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/record")
public class RecordCreationController {
    private final RecordCreationService recordCreationService;

    @PostMapping("/{recordPageId}/{recordPhotoId}")
    public ResponseEntity<RecordResponseDTO.RecordResultDTO> createOrUpdateRecord(
            @PathVariable Long recordPageId,
            @PathVariable Long recordPhotoId,
            @RequestBody RecordRequestDTO.RecordDTO recordDTO) {
        RecordResponseDTO.RecordResultDTO createdBody = recordCreationService.createOrUpdateRecord(recordPageId, recordPhotoId, recordDTO);
        return ResponseEntity.ok(createdBody);
    }
}
