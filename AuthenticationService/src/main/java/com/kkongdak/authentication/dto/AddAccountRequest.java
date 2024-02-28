package com.kkongdak.authentication.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class AddAccountRequest {
    @Email(message = "이메일 형식이 아닙니다")
    private String email;

    //TODO: 비밀번호 조건 생성
    @Length(min = 8, message = "비밀번호는 최소 8글자 이상이여야 합니다")
    private String password;
}
