package com.leafgo.service;

import com.leafgo.dto.LoginRequest;
import com.leafgo.dto.RegisterRequest;
import com.leafgo.entity.User;
import com.leafgo.repository.UserRepository;
import com.leafgo.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String identifier = request.getPhone() != null ? request.getPhone() : request.getEmail();

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("手机号/邮箱已被注册");
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("用户名已被使用");
        }

        if (request.getPhone() != null && userRepository.existsByPhone(request.getPhone())) {
            throw new RuntimeException("手机号已被注册");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setUserType(User.UserType.valueOf(request.getUserType().toUpperCase()));
        user.setStatus(User.UserStatus.ACTIVE);

        user = userRepository.save(user);

        String token = jwtTokenProvider.generateToken(user.getId(), user.getEmail(), user.getUserType().name());

        log.info("用户注册成功: {}", request.getEmail());

        return new AuthResponse(user.getId(), token, user.getUserType().name());
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseGet(() -> userRepository.findByPhone(request.getEmail())
                        .orElseThrow(() -> new RuntimeException("手机号/邮箱或密码错误")));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("手机号/邮箱或密码错误");
        }

        if (user.getStatus() == User.UserStatus.BANNED) {
            throw new RuntimeException("账号已被封禁");
        }

        String token = jwtTokenProvider.generateToken(user.getId(), user.getEmail(), user.getUserType().name());

        log.info("用户登录成功: {}", user.getEmail());

        return new AuthResponse(user.getId(), token, user.getUserType().name());
    }

    public record AuthResponse(Long userId, String token, String userType) {}
}
