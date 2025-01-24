package com.example.todolist.board.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class BoardPasswordRequestDto {

    @NotBlank(message = "비밀번호는 필수 입력 사항입니다.")
    @Size(min = 1, max = 4, message = "비밀번호는 1 ~ 4자리만 입력가능합니다.")
    @Schema(description = "비밀번호 입력 (필수, 1 ~ 4자리 사이만 가능)", example = "1234")
    private String password;
}
