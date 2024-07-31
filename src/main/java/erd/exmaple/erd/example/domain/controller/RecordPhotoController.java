package erd.exmaple.erd.example.domain.controller;

import erd.exmaple.erd.example.domain.dto.RecordPhotoRequestDTO;
import erd.exmaple.erd.example.domain.dto.RecordPhotoResponseDTO;
import erd.exmaple.erd.example.domain.service.RecordService.RecordPhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/add")
public class RecordPhotoController {
    private final RecordPhotoService recordPhotoService;

    @PostMapping("/{recordPageId}/recordPhoto")
    public ResponseEntity<List<RecordPhotoResponseDTO.PhotoResponseDTO>> receiveRecordPhoto(@PathVariable Long recordPageId, @RequestBody RecordPhotoRequestDTO.PhotoRequestDTO recordPhotoRequestDTO) {
        List<RecordPhotoResponseDTO.PhotoResponseDTO> receivePhoto = recordPhotoService.receiveRecordPhoto(recordPageId, recordPhotoRequestDTO);
        return ResponseEntity.ok(receivePhoto);
    }
}
