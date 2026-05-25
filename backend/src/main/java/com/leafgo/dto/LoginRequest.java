package com.leafgo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "手机号/邮箱不能为空")
    private String email;

    @NotBlank(message = "密码不能为空")
    private String password;
}
