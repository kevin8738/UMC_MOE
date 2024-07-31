package erd.exmaple.erd.example.domain.controller;

import erd.exmaple.erd.example.domain.dto.RecordPageResponseDTO;
import erd.exmaple.erd.example.domain.service.RecordService.RecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/record/{userId}")
public class RecordOldestController {
    private final RecordService recordService;

    @GetMapping("/oldest")
    public List<RecordPageResponseDTO> getOldestRecordList(@PathVariable Long userId, Pageable pageable) {
        return recordService.getPagedRecordsDto(PageRequest.of(pageable.getPageNumber(), 4), true, userId).getContent();
    }
}
