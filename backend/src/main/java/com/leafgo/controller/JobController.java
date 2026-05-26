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
@RequestMapping("/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobRepository jobRepository;
    private final CompanyRepository companyRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> getJobs(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String jobType,
            @RequestParam(required = false) String salary,
            @RequestParam(required = false) String experience,
            @RequestParam(required = false) String education) {

        Pageable pageable = PageRequest.of(Math.max(0, page - 1), size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Job> jobPage = jobRepository.searchJobs(keyword, city, jobType, salary, experience, education, pageable);

        Map<String, Object> result = new HashMap<>();
        result.put("content", jobPage.getContent());
        result.put("totalElements", jobPage.getTotalElements());
        result.put("totalPages", jobPage.getTotalPages());
        result.put("size", jobPage.getSize());
        result.put("number", jobPage.getNumber() + 1);

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Map<String, Object>>> searchJobs(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String jobType,
            @RequestParam(required = false) String salary,
            @RequestParam(required = false) String experience,
            @RequestParam(required = false) String education) {

        Pageable pageable = PageRequest.of(Math.max(0, page - 1), size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Job> jobPage = jobRepository.searchJobs(keyword, city, jobType, salary, experience, education, pageable);

        Map<String, Object> result = new HashMap<>();
        result.put("content", jobPage.getContent());
        result.put("totalElements", jobPage.getTotalElements());
        result.put("totalPages", jobPage.getTotalPages());
        result.put("size", jobPage.getSize());
        result.put("number", jobPage.getNumber() + 1);

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getMyJobs(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String status) {
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("请先登录"));
        }

        Pageable pageable = PageRequest.of(Math.max(0, page - 1), size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Job> jobPage;

        Company company = companyRepository.findByUserId(userId).orElse(null);
        if (company == null) {
            jobPage = Page.empty();
        } else if (status != null && !status.isEmpty()) {
            try {
                Job.JobStatus jobStatus = Job.JobStatus.valueOf(status.toUpperCase());
                jobPage = jobRepository.findByCompanyIdAndStatus(company.getId(), jobStatus, pageable);
            } catch (IllegalArgumentException e) {
                jobPage = jobRepository.findByCompanyId(company.getId(), pageable);
            }
        } else {
            jobPage = jobRepository.findByCompanyId(company.getId(), pageable);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("content", jobPage.getContent());
        result.put("totalElements", jobPage.getTotalElements());
        result.put("totalPages", jobPage.getTotalPages());
        result.put("size", jobPage.getSize());
        result.put("number", jobPage.getNumber() + 1);

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Job>> getJobDetail(@PathVariable Long id) {
        return jobRepository.findById(id)
                .map(job -> ResponseEntity.ok(ApiResponse.success(job)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Job>> createJob(@RequestBody Job job) {
        Long userId = SecurityUtil.getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("请先登录"));
        }
        Company company = companyRepository.findByUserId(userId).orElse(null);
        if (company == null) {
            return ResponseEntity.badRequest().body(ApiResponse.error("请先完善企业信息"));
        }
        job.setCompanyId(company.getId());
        Job savedJob = jobRepository.save(job);
        return ResponseEntity.ok(ApiResponse.success("职位创建成功", savedJob));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Job>> updateJob(@PathVariable Long id, @RequestBody Job job) {
        return jobRepository.findById(id)
                .map(existingJob -> {
                    job.setId(id);
                    job.setCompanyId(existingJob.getCompanyId());
                    Job updatedJob = jobRepository.save(job);
                    return ResponseEntity.ok(ApiResponse.success("职位更新成功", updatedJob));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteJob(@PathVariable Long id) {
        if (jobRepository.existsById(id)) {
            jobRepository.deleteById(id);
            return ResponseEntity.ok(ApiResponse.success("职位删除成功", null));
        }
        return ResponseEntity.notFound().build();
    }
}
