package com.leafgo.controller;

import com.leafgo.entity.Banner;
import com.leafgo.repository.BannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/banners")
@RequiredArgsConstructor
public class BannerController {

    private final BannerRepository bannerRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Banner>>> getBanners() {
        List<Banner> banners = bannerRepository.findByActiveTrueOrderBySortOrderAsc();
        return ResponseEntity.ok(ApiResponse.success(banners));
    }
}
