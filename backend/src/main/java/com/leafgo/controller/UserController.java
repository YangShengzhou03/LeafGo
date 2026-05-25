package com.leafgo.controller;

import com.leafgo.entity.User;
import com.leafgo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> getUserInfo(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(user -> ResponseEntity.ok(ApiResponse.success(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> updateUserInfo(
            @PathVariable Long id,
            @RequestBody User user) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    user.setId(id);
                    user.setPassword(existingUser.getPassword());
                    user.setEmail(existingUser.getEmail());
                    user.setUserType(existingUser.getUserType());
                    user.setCreatedAt(existingUser.getCreatedAt());
                    User updatedUser = userRepository.save(user);
                    return ResponseEntity.ok(ApiResponse.success("用户信息更新成功", updatedUser));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/avatar")
    public ResponseEntity<ApiResponse<String>> uploadAvatar(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        String url = "https://example.com/avatars/" + fileName;
        return ResponseEntity.ok(ApiResponse.success("上传成功", url));
    }
}
