package erd.exmaple.erd.example.domain.controller;

import erd.exmaple.erd.example.domain.PhotoEntity;
import erd.exmaple.erd.example.domain.Record_PageEntity;
import erd.exmaple.erd.example.domain.Record_PhotoEntity;
import erd.exmaple.erd.example.domain.Record_PhotoBodyEntity;
import erd.exmaple.erd.example.domain.dto.ExhibitionOrPopupDetailsDTO;
import erd.exmaple.erd.example.domain.dto.RecordPageResponseDTO;
import erd.exmaple.erd.example.domain.dto.RecordPhotoDetailsDTO;
import erd.exmaple.erd.example.domain.service.RecordService.RecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/record/{userId}")
public class RecordLatestController {
    private final RecordService recordService;

    @GetMapping("/latest")
    public List<RecordPageResponseDTO> getLatestRecordList(@PathVariable Long userId, Pageable pageable) {
        return recordService.getPagedRecordsDto(PageRequest.of(pageable.getPageNumber(), 4), false, userId).getContent();
    }

    @GetMapping("/{pageId}")
    public ExhibitionOrPopupDetailsDTO getExhibitionOrPopupDetails(@PathVariable Long userId, @PathVariable Long pageId) {
        return recordService.getExhibitionOrPopupDetails(userId, pageId);
    }

    @GetMapping("/{pageId}/photo/{photoId}")
    public RecordPhotoDetailsDTO getRecordPhotoDetails(@PathVariable Long userId, @PathVariable Long pageId, @PathVariable Long photoId) {
        return recordService.getRecordPhotoDetails(userId, photoId);
    }
}
