package com.leafgo.controller;

import com.leafgo.entity.Application;
import com.leafgo.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationRepository applicationRepository;

    @PostMapping
    public ResponseEntity<ApiResponse<Application>> createApplication(@RequestBody Application application) {
        application.setStatus(Application.ApplicationStatus.PENDING);
        Application saved = applicationRepository.save(application);
        return ResponseEntity.ok(ApiResponse.success("投递成功", saved));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> getMyApplications(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String status) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Application> applicationPage;
        if (status != null && !status.isEmpty()) {
            applicationPage = applicationRepository.findByJobId(Long.parseLong(status), pageable);
        } else {
            applicationPage = applicationRepository.findAll(pageable);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("content", applicationPage.getContent());
        result.put("totalElements", applicationPage.getTotalElements());
        result.put("totalPages", applicationPage.getTotalPages());
        result.put("size", applicationPage.getSize());
        result.put("number", applicationPage.getNumber() + 1);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Application>> getApplicationDetail(@PathVariable Long id) {
        return applicationRepository.findById(id)
                .map(app -> ResponseEntity.ok(ApiResponse.success(app)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<Application>> updateApplicationStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        return applicationRepository.findById(id)
                .map(existing -> {
                    existing.setStatus(Application.ApplicationStatus.valueOf(body.get("status")));
                    Application updated = applicationRepository.save(existing);
                    return ResponseEntity.ok(ApiResponse.success("状态更新成功", updated));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/received")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getReceivedApplications(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Application> applicationPage = applicationRepository.findAll(pageable);
        Map<String, Object> result = new HashMap<>();
        result.put("content", applicationPage.getContent());
        result.put("totalElements", applicationPage.getTotalElements());
        result.put("totalPages", applicationPage.getTotalPages());
        result.put("size", applicationPage.getSize());
        result.put("number", applicationPage.getNumber() + 1);
        return ResponseEntity.ok(ApiResponse.success(result));
    }
}
