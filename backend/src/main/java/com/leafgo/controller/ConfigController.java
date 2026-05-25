package com.leafgo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/config")
public class ConfigController {

    @GetMapping("/hot-keywords")
    public ResponseEntity<ApiResponse<List<String>>> getHotKeywords() {
        List<String> keywords = Arrays.asList(
            "Java开发", "前端开发", "产品经理", "运营",
            "UI设计", "Python", "数据分析", "测试工程师"
        );
        return ResponseEntity.ok(ApiResponse.success(keywords));
    }
}
