package erd.exmaple.erd.example.domain.service.SpecService;

import erd.exmaple.erd.example.domain.ExhibitionEntity;
import erd.exmaple.erd.example.domain.repository.ExhibitionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SpecExhibitionService {
    private final ExhibitionRepository exhibitionRepository;
    public ExhibitionEntity findById(Long id) {
        return exhibitionRepository.findById(id).orElse(null);
    }

}
