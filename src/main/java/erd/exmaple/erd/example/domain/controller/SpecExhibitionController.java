package erd.exmaple.erd.example.domain.controller;

import erd.exmaple.erd.example.domain.ExhibitionEntity;
import erd.exmaple.erd.example.domain.dto.ExhibitionResponseDTO;
import erd.exmaple.erd.example.domain.service.SpecService.SpecExhibitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
public class SpecExhibitionController {
    private final SpecExhibitionService specExhibitionService;


    @GetMapping("/exhibitions/{id}")
    public ResponseEntity<ExhibitionResponseDTO> getExhibition(@PathVariable("id") Long id) {
        ExhibitionEntity exhibitionEntity = specExhibitionService.findById(id);

        return ResponseEntity.ok().body(new ExhibitionResponseDTO(exhibitionEntity));

    }


}
