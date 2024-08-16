package erd.exmaple.erd.example.domain.controller;

import erd.exmaple.erd.example.domain.ExhibitionEntity;
import erd.exmaple.erd.example.domain.Popup_StoreEntity;
import erd.exmaple.erd.example.domain.dto.ExhibitionResponseDTO;
import erd.exmaple.erd.example.domain.dto.PopupStoreResponseDTO;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        String districts = (String) session.getAttribute("filterDistricts");
        List<District> districtList = null;
        if (districts != null && !districts.isEmpty()) {
            districtList = Stream.of(districts.split(","))
                    .map(District::valueOf)
                    .collect(Collectors.toList());
        }

        List<ExhibitionEntity> exhibitions;
        List<Popup_StoreEntity> popupStores;

        if (region == null && (districtList == null || districtList.isEmpty())) {
            exhibitions = regionService.getAllExhibitions();
            popupStores = regionService.getAllPopupStores();
        } else if (region != null && (districtList == null || districtList.isEmpty())) {
            exhibitions = regionService.getExhibitionsByRegion(region);
            popupStores = regionService.getPopupStoresByRegion(region);
        } else {
            exhibitions = districtList.stream()
                    .flatMap(district -> regionService.getExhibitionsByDistrict(district).stream())
                    .collect(Collectors.toList());
            popupStores = districtList.stream()
                    .flatMap(district -> regionService.getPopupStoresByDistrict(district).stream())
                    .collect(Collectors.toList());
        }

        model.addAttribute("exhibitions", exhibitions);
        model.addAttribute("popupStores", popupStores);

        return "main";
    }

    @GetMapping("/main/filter")
    @ResponseBody
    public FilterResultsDTO filterResults(@RequestParam(name = "region", required = false) Region region,
                                          @RequestParam(name = "districts", required = false) String districts,
                                          Pageable pageable) {
        List<District> districtList = null;
        if (districts != null && !districts.isEmpty()) {
            districtList = Stream.of(districts.split(","))
                    .map(District::valueOf)
                    .collect(Collectors.toList());
        }

        Page<ExhibitionEntity> topLikedExhibitions;
        Page<ExhibitionEntity> latestExhibitions;
        Page<Popup_StoreEntity> topLikedPopupStores;
        Page<Popup_StoreEntity> latestPopupStores;

        if (region == null && (districtList == null || districtList.isEmpty())) {
            topLikedExhibitions = regionService.getAllTopLikedExhibitions(pageable);
            latestExhibitions = regionService.getAllLatestExhibitions(pageable);
            topLikedPopupStores = regionService.getAllTopLikedPopupStores(pageable);
            latestPopupStores = regionService.getAllLatestPopupStores(pageable);
        } else if (region != null && (districtList == null || districtList.isEmpty())) {
            topLikedExhibitions = regionService.getTopLikedExhibitionsByRegion(region, pageable);
            latestExhibitions = regionService.getLatestExhibitionsByRegion(region, pageable);
            topLikedPopupStores = regionService.getTopLikedPopupStoresByRegion(region, pageable);
            latestPopupStores = regionService.getLatestPopupStoresByRegion(region, pageable);
        } else {
            topLikedExhibitions = districtList.stream()
                    .flatMap(district -> regionService.getTopLikedExhibitionsByDistrict(district, pageable).getContent().stream())
                    .collect(Collectors.collectingAndThen(Collectors.toList(), list -> new PageImpl<>(list, pageable, list.size())));
            latestExhibitions = districtList.stream()
                    .flatMap(district -> regionService.getLatestExhibitionsByDistrict(district, pageable).getContent().stream())
                    .collect(Collectors.collectingAndThen(Collectors.toList(), list -> new PageImpl<>(list, pageable, list.size())));
            topLikedPopupStores = districtList.stream()
                    .flatMap(district -> regionService.getTopLikedPopupStoresByDistrict(district, pageable).getContent().stream())
                    .collect(Collectors.collectingAndThen(Collectors.toList(), list -> new PageImpl<>(list, pageable, list.size())));
            latestPopupStores = districtList.stream()
                    .flatMap(district -> regionService.getLatestPopupStoresByDistrict(district, pageable).getContent().stream())
                    .collect(Collectors.collectingAndThen(Collectors.toList(), list -> new PageImpl<>(list, pageable, list.size())));
        }

        List<ExhibitionResponseDTO> topLikedExhibitionsDTO = topLikedExhibitions.getContent().stream().map(ExhibitionResponseDTO::new).collect(Collectors.toList());
        List<ExhibitionResponseDTO> latestExhibitionsDTO = latestExhibitions.getContent().stream().map(ExhibitionResponseDTO::new).collect(Collectors.toList());
        List<PopupStoreResponseDTO> topLikedPopupStoresDTO = topLikedPopupStores.getContent().stream().map(PopupStoreResponseDTO::new).collect(Collectors.toList());
        List<PopupStoreResponseDTO> latestPopupStoresDTO = latestPopupStores.getContent().stream().map(PopupStoreResponseDTO::new).collect(Collectors.toList());

        return new FilterResultsDTO(topLikedExhibitionsDTO, latestExhibitionsDTO, topLikedPopupStoresDTO, latestPopupStoresDTO);
    }
}







