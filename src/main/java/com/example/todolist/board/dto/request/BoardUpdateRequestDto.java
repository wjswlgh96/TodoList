package com.example.todolist.board.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class BoardUpdateRequestDto {

    @NotBlank(message = "비밀번호는 필수 입력 사항입니다.")
    @Size(min = 1, max = 4, message = "비밀번호는 1 ~ 4자리 사이로 만들어야 합니다.")
    @Schema(description = "비밀번호 입력 (필수, 1 ~ 4자리 사이만 가능)", example = "1234")
    private String password;

    @NotBlank(message = "제목은 필수 입력 사항입니다.")
    @Size(max = 50, message = "제목은 최대 50자 이내로 입력해야 합니다.")
    @Schema(description = "제목 입력 (필수, 최대 50자)", example = "수정된 제목입니다!!")
    private String title;

    @NotBlank(message = "할일 내용은 필수 입력 사항입니다.")
    @Size(max = 200, message = "할일 내용은 최대 200자 이내로 입력해야 합니다.")
    @Schema(description = "할일 입력 (필수, 최대 200자)", example = "수정된 오늘의 할일입니다!!")
    private String contents;
}
