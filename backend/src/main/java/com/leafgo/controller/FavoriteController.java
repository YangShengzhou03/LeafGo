package com.leafgo.controller;

import com.leafgo.entity.Favorite;
import com.leafgo.repository.FavoriteRepository;
import com.leafgo.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteRepository favoriteRepository;

    @PostMapping("/{jobId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> addFavorite(@PathVariable Long jobId) {
        Long userId = SecurityUtil.getCurrentUserId();
        
        if (userId == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("请先登录"));
        }
        
        if (favoriteRepository.existsByUserIdAndJobId(userId, jobId)) {
            return ResponseEntity.ok(ApiResponse.error("已经收藏过该职位"));
        }
        
        Favorite favorite = new Favorite();
        favorite.setUserId(userId);
        favorite.setJobId(jobId);
        favoriteRepository.save(favorite);
        
        Map<String, Object> result = new HashMap<>();
        result.put("favorited", true);
        result.put("jobId", jobId);
        
        return ResponseEntity.ok(ApiResponse.success("收藏成功", result));
    }

    @DeleteMapping("/{jobId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> removeFavorite(@PathVariable Long jobId) {
        Long userId = SecurityUtil.getCurrentUserId();
        
        if (userId == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("请先登录"));
        }
        
        favoriteRepository.deleteByUserIdAndJobId(userId, jobId);
        
        Map<String, Object> result = new HashMap<>();
        result.put("favorited", false);
        result.put("jobId", jobId);
        
        return ResponseEntity.ok(ApiResponse.success("取消收藏成功", result));
    }

    @GetMapping("/{jobId}/status")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getFavoriteStatus(@PathVariable Long jobId) {
        Long userId = SecurityUtil.getCurrentUserId();
        
        if (userId == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("请先登录"));
        }
        
        boolean isFavorited = favoriteRepository.existsByUserIdAndJobId(userId, jobId);
        
        Map<String, Object> result = new HashMap<>();
        result.put("favorited", isFavorited);
        result.put("jobId", jobId);
        
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Favorite>>> getMyFavorites() {
        Long userId = SecurityUtil.getCurrentUserId();
        
        if (userId == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("请先登录"));
        }
        
        List<Favorite> favorites = favoriteRepository.findByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(favorites));
    }
}
