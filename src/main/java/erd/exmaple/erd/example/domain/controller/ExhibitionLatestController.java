package erd.exmaple.erd.example.domain.controller;

import erd.exmaple.erd.example.domain.ExhibitionEntity;
import erd.exmaple.erd.example.domain.service.SpecService.ExhibitionLatestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exhibitions")
public class ExhibitionLatestController {

    @Autowired
    private ExhibitionLatestService exhibitionLatestService;


    @GetMapping("/latest")
    public ResponseEntity<Page<ExhibitionEntity>> getAllExhibitions(Pageable pageable) {
        Page<ExhibitionEntity> exhibitions = exhibitionLatestService.getAllExhibitions(pageable);
        return ResponseEntity.ok(exhibitions);
    }
}
