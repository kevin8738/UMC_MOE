package erd.exmaple.erd.example.domain.controller;


import erd.exmaple.erd.example.domain.service.FollowService;
import erd.exmaple.erd.example.domain.dto.FollowDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/follows")
public class FollowController {

    @Autowired
    private FollowService followService;

    @GetMapping("/page/{userId}")
    public String getFollowPage(@PathVariable Long userId, Model model) {
        // 사용자 ID를 모델에 추가
        model.addAttribute("userId", userId);
        return "followPage"; // followPage.html 템플릿을 반환
    }

    @GetMapping("/latest/{userId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getUserFollowsByLatest(@PathVariable Long userId, @PageableDefault(size = 5) Pageable pageable) {
        try {
            Page<FollowDTO> followsPage = followService.getUserFollowsByLatest(userId, pageable);

            Map<String, Object> response = new HashMap<>();
            response.put("follows", followsPage.getContent());
            response.put("totalPages", followsPage.getTotalPages());
            response.put("totalElements", followsPage.getTotalElements());
            response.put("currentPage", followsPage.getNumber());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null); // 500 에러 반환
        }
    }

    @GetMapping("/oldest/{userId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getUserFollowsByOldest(@PathVariable Long userId, @PageableDefault(size = 5) Pageable pageable) {
        try {
            Page<FollowDTO> followsPage = followService.getUserFollowsByOldest(userId, pageable);

            Map<String, Object> response = new HashMap<>();
            response.put("follows", followsPage.getContent());
            response.put("totalPages", followsPage.getTotalPages());
            response.put("totalElements", followsPage.getTotalElements());
            response.put("currentPage", followsPage.getNumber());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null); // 500 에러 반환
        }
    }

    @PostMapping("/followExhibition")
    @ResponseBody
    public ResponseEntity<FollowDTO> followExhibition(@RequestParam Long userId, @RequestParam Long exhibitionId) {
        try {
            return ResponseEntity.ok(followService.followExhibition(userId, exhibitionId));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null); // 500 에러 반환
        }
    }

    @PostMapping("/followPopupStore")
    @ResponseBody
    public ResponseEntity<FollowDTO> followPopupStore(@RequestParam Long userId, @RequestParam Long popupStoreId) {
        try {
            return ResponseEntity.ok(followService.followPopupStore(userId, popupStoreId));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null); // 500 에러 반환
        }
    }

    @DeleteMapping("/unfollow/{followId}")
    @ResponseBody
    public ResponseEntity<Void> unfollow(@PathVariable Long followId) {
        try {
            followService.unfollow(followId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build(); // 500 에러 반환
        }
    }

    @PostMapping("/toggleHeart/{followId}")
    @ResponseBody
    public ResponseEntity<FollowDTO> toggleHeart(@PathVariable Long followId) {
        try {
            return ResponseEntity.ok(followService.toggleHeart(followId));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null); // 500 에러 반환
        }
    }
}