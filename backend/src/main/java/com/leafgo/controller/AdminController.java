package com.leafgo.controller;

import com.leafgo.entity.Company;
import com.leafgo.entity.Job;
import com.leafgo.entity.User;
import com.leafgo.repository.ApplicationRepository;
import com.leafgo.repository.CompanyRepository;
import com.leafgo.repository.JobRepository;
import com.leafgo.repository.UserRepository;
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
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final JobRepository jobRepository;
    private final ApplicationRepository applicationRepository;

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("userCount", userRepository.count());
        stats.put("companyCount", companyRepository.count());
        stats.put("jobCount", jobRepository.count());
        stats.put("applicationCount", applicationRepository.count());
        return ResponseEntity.ok(ApiResponse.success(stats));
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String keyword) {
        Pageable pageable = PageRequest.of(Math.max(0, page - 1), size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<User> userPage;
        if (keyword != null && !keyword.isEmpty()) {
            userPage = userRepository.findByUsernameContaining(keyword, pageable);
        } else {
            userPage = userRepository.findAll(pageable);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("content", userPage.getContent());
        result.put("totalElements", userPage.getTotalElements());
        result.put("totalPages", userPage.getTotalPages());
        result.put("size", userPage.getSize());
        result.put("number", userPage.getNumber() + 1);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @PutMapping("/users/{id}/status")
    public ResponseEntity<ApiResponse<User>> toggleUserStatus(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    if (user.getStatus() == User.UserStatus.ACTIVE) {
                        user.setStatus(User.UserStatus.INACTIVE);
                    } else {
                        user.setStatus(User.UserStatus.ACTIVE);
                    }
                    User updated = userRepository.save(user);
                    return ResponseEntity.ok(ApiResponse.success("状态更新成功", updated));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/companies")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getCompanies(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(Math.max(0, page - 1), size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Company> companyPage = companyRepository.findAll(pageable);
        Map<String, Object> result = new HashMap<>();
        result.put("content", companyPage.getContent());
        result.put("totalElements", companyPage.getTotalElements());
        result.put("totalPages", companyPage.getTotalPages());
        result.put("size", companyPage.getSize());
        result.put("number", companyPage.getNumber() + 1);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/jobs")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getJobs(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(Math.max(0, page - 1), size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Job> jobPage = jobRepository.findAll(pageable);
        Map<String, Object> result = new HashMap<>();
        result.put("content", jobPage.getContent());
        result.put("totalElements", jobPage.getTotalElements());
        result.put("totalPages", jobPage.getTotalPages());
        result.put("size", jobPage.getSize());
        result.put("number", jobPage.getNumber() + 1);
        return ResponseEntity.ok(ApiResponse.success(result));
    }
}
