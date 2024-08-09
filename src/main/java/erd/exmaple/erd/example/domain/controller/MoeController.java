package erd.exmaple.erd.example.domain.controller;

import erd.exmaple.erd.example.domain.dto.ExhibitionDTO;
import erd.exmaple.erd.example.domain.dto.PopupStoreDTO;
import erd.exmaple.erd.example.domain.dto.FilterResultsDTO;
import erd.exmaple.erd.example.domain.dto.UserDTO;
import erd.exmaple.erd.example.domain.enums.District;
import erd.exmaple.erd.example.domain.enums.Region;
import erd.exmaple.erd.example.domain.service.RegionService;
import erd.exmaple.erd.example.domain.service.UserService.UserServiceSocial;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class MoeController {

    private static final Logger log = LoggerFactory.getLogger(MoeController.class);

    private final UserServiceSocial userServiceSocial;
    private final RegionService regionService;

    @GetMapping
    public String redirectToLogin() {
        log.info("Redirecting to login page from Moe");
        return "redirect:/user/login";
    }

    @PostMapping("/main")
    @ResponseBody
    public UserDTO login(@RequestBody Long id) {
        UserDTO userDto = userServiceSocial.findUserById(id);
        log.info("Main page accessed by user id: {}", userDto.getId());
        return userDto;
    }

    @GetMapping("/main")
    public String mainPage(Model model, HttpSession session) {
        // 세션에서 필터링 조건을 가져옴
        Region region = (Region) session.getAttribute("filterRegion");
        String district = (String) session.getAttribute("filterDistrict");

        List<ExhibitionDTO> exhibitions;
        List<PopupStoreDTO> popupStores;

        if (region == null && district == null) {
            exhibitions = regionService.getAllExhibitions();
            popupStores = regionService.getAllPopupStores();
        } else if (region != null && (district == null || district.isEmpty())) {
            exhibitions = regionService.getExhibitionsByRegion(region);
            popupStores = regionService.getPopupStoresByRegion(region);
        } else {
            exhibitions = regionService.getExhibitionsByDistrict(district);
            popupStores = regionService.getPopupStoresByDistrict(district);
        }

        model.addAttribute("exhibitions", exhibitions);
        model.addAttribute("popupStores", popupStores);

        return "main";
    }

    @GetMapping("/main/filter")
    @ResponseBody
    public FilterResultsDTO filterResults(@RequestParam(required = false) Region region,
                                          @RequestParam(required = false) String district,
                                          Pageable pageable) {
        Page<ExhibitionDTO> topLikedExhibitions;
        Page<ExhibitionDTO> latestExhibitions;
        Page<PopupStoreDTO> topLikedPopupStores;
        Page<PopupStoreDTO> latestPopupStores;

        if (region == null && district == null) {
            topLikedExhibitions = regionService.getAllTopLikedExhibitions(pageable);
            latestExhibitions = regionService.getAllLatestExhibitions(pageable);
            topLikedPopupStores = regionService.getAllTopLikedPopupStores(pageable);
            latestPopupStores = regionService.getAllLatestPopupStores(pageable);
        } else if (region != null && (district == null || district.isEmpty())) {
            topLikedExhibitions = regionService.getTopLikedExhibitionsByRegion(region, pageable);
            latestExhibitions = regionService.getLatestExhibitionsByRegion(region, pageable);
            topLikedPopupStores = regionService.getTopLikedPopupStoresByRegion(region, pageable);
            latestPopupStores = regionService.getLatestPopupStoresByRegion(region, pageable);
        } else {
            topLikedExhibitions = regionService.getTopLikedExhibitionsByDistrict(District.valueOf(district), pageable);
            latestExhibitions = regionService.getLatestExhibitionsByDistrict(District.valueOf(district), pageable);
            topLikedPopupStores = regionService.getTopLikedPopupStoresByDistrict(District.valueOf(district), pageable);
            latestPopupStores = regionService.getLatestPopupStoresByDistrict(District.valueOf(district), pageable);
        }

        return new FilterResultsDTO(topLikedExhibitions.getContent(), latestExhibitions.getContent(), topLikedPopupStores.getContent(), latestPopupStores.getContent());
    }
}


