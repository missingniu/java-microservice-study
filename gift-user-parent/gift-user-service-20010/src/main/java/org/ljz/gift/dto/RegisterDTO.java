package org.ljz.gift.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDTO {
    @NotBlank(message = "电话号码不能为空")
    private String mobilePhone;
    @NotBlank(message = "验证码不能为空")
    private String verifyCode;
    @NotBlank(message = "密码不能为空")
    private String password;
}
