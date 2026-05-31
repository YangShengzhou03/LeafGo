package com.leafgo.controller;

import com.leafgo.entity.Company;
import com.leafgo.entity.Job;
import com.leafgo.repository.CompanyRepository;
import com.leafgo.repository.JobRepository;
import com.leafgo.security.SecurityUtil;
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
@RequestMapping("/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyRepository companyRepository;
    private final JobRepository jobRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> getCompanies(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String industry,
            @RequestParam(required = false) String keyword) {

        Pageable pageable = PageRequest.of(Math.max(0, page - 1), size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Company> companyPage;

        if (keyword != null && !keyword.isEmpty()) {
            companyPage = companyRepository.findByNameContaining(keyword, pageable);
        } else if (industry != null && !industry.isEmpty()) {
            companyPage = companyRepository.findByIndustry(industry, pageable);
        } else {
            companyPage = companyRepository.findAll(pageable);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("content", companyPage.getContent());
        result.put("totalElements", companyPage.getTotalElements());
        result.put("totalPages", companyPage.getTotalPages());
        result.put("size", companyPage.getSize());
        result.put("number", companyPage.getNumber() + 1);

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<Company>> getMyCompany() {
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("请先登录"));
        }
        return companyRepository.findByUserId(userId)
                .map(company -> ResponseEntity.ok(ApiResponse.success(company)))
                .orElse(ResponseEntity.ok(ApiResponse.success(null)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Company>> getCompanyDetail(@PathVariable Long id) {
        return companyRepository.findById(id)
                .map(company -> ResponseEntity.ok(ApiResponse.success(company)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Company>> updateCompany(
            @PathVariable Long id,
            @RequestBody Company company) {
        return companyRepository.findById(id)
                .map(existingCompany -> {
                    company.setId(id);
                    company.setUserId(existingCompany.getUserId());
                    Company updatedCompany = companyRepository.save(company);
                    return ResponseEntity.ok(ApiResponse.success("公司信息更新成功", updatedCompany));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Company>> createCompany(@RequestBody Company company) {
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("请先登录"));
        }
        
        if (companyRepository.findByUserId(userId).isPresent()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("您已创建过企业"));
        }
        
        company.setUserId(userId);
        Company savedCompany = companyRepository.save(company);
        return ResponseEntity.ok(ApiResponse.success("企业创建成功", savedCompany));
    }

    @GetMapping("/{id}/jobs")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getCompanyJobs(
            @PathVariable Long id,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(Math.max(0, page - 1), size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Job> jobPage = jobRepository.findByCompanyId(id, pageable);

        Map<String, Object> result = new HashMap<>();
        result.put("content", jobPage.getContent());
        result.put("totalElements", jobPage.getTotalElements());
        result.put("totalPages", jobPage.getTotalPages());
        result.put("size", jobPage.getSize());
        result.put("number", jobPage.getNumber() + 1);

        return ResponseEntity.ok(ApiResponse.success(result));
    }
}
