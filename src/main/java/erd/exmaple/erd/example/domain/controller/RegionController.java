package erd.exmaple.erd.example.domain.controller;

import erd.exmaple.erd.example.domain.enums.Region;
import erd.exmaple.erd.example.domain.service.RegionService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/regions")
public class RegionController {

    private final RegionService regionService;

    @GetMapping("/filter")
    public String filterPage(Model model) {
        model.addAttribute("regions", Region.values());
        return "filter";
    }

    @PostMapping("/filter")
    public String setFilter(@RequestParam(required = false) Region region,
                            @RequestParam(required = false) String district,
                            HttpSession session) {
        session.setAttribute("filterRegion", region);
        session.setAttribute("filterDistrict", district);
        return "redirect:/Moe/main";
    }

    @GetMapping("/districts/{region}")
    @ResponseBody
    public List<String> getDistrictsByRegion(@PathVariable Region region) {
        return regionService.getDistrictsByRegion(region).stream()
                .map(Enum::name)
                .collect(Collectors.toList());
    }
}