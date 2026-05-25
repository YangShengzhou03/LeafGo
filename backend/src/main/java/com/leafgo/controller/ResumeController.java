package com.leafgo.controller;

import com.leafgo.entity.Resume;
import com.leafgo.repository.ResumeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/resumes")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeRepository resumeRepository;

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<Resume>> getResume(@PathVariable Long userId) {
        return resumeRepository.findByUserId(userId)
                .map(resume -> ResponseEntity.ok(ApiResponse.success(resume)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<Resume>> updateResume(
            @PathVariable Long userId,
            @RequestBody Resume resume) {
        return resumeRepository.findByUserId(userId)
                .map(existingResume -> {
                    resume.setId(existingResume.getId());
                    resume.setUserId(userId);
                    Resume updatedResume = resumeRepository.save(resume);
                    return ResponseEntity.ok(ApiResponse.success("简历更新成功", updatedResume));
                })
                .orElseGet(() -> {
                    resume.setUserId(userId);
                    Resume savedResume = resumeRepository.save(resume);
                    return ResponseEntity.ok(ApiResponse.success("简历创建成功", savedResume));
                });
    }

    @PostMapping("/{userId}/upload")
    public ResponseEntity<ApiResponse<String>> uploadResumeFile(
            @PathVariable Long userId,
            @RequestParam("file") MultipartFile file) {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        String url = "https://example.com/resumes/" + fileName;
        return ResponseEntity.ok(ApiResponse.success("上传成功", url));
    }
}
