package erd.exmaple.erd.example.domain.controller;

import erd.exmaple.erd.example.domain.ExhibitionEntity;
import erd.exmaple.erd.example.domain.service.SpecService.ExhibitionPopularService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exhibitions")
public class ExhibitionPopularController {

    @Autowired
    private ExhibitionPopularService exhibitionPopularService;

    /**
     * 좋아요 순으로 전시회를 조회합니다.
     *
     * @param pageable 페이지 정보
     * @return 좋아요 순으로 정렬된 전시회 목록
     */
    @GetMapping("/top-liked")
    public ResponseEntity<Page<ExhibitionEntity>> getTopLikedExhibitions(Pageable pageable) {
        Page<ExhibitionEntity> exhibitions = exhibitionPopularService.getTopLikedExhibitions(pageable);
        return ResponseEntity.ok(exhibitions);
    }
}
