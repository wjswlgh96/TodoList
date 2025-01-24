package com.example.todolist.author.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AuthorRequestDto {
    @Schema(hidden = true)
    private Long id;

    @NotBlank(message = "이름은 필수 입력 사항입니다.")
    @Schema(description = "작성자의 이름 (필수)", example = "홍길동")
    private String name;

    @NotBlank(message = "이메일은 필수 입력 사항입니다.")
    @Email(message = "올바른 이메일 형식을 입력해주세요")
    @Schema(description = "작성자의 이메일 (필수, 중복 불가)", example = "hong@example.com")
    private String email;
}
