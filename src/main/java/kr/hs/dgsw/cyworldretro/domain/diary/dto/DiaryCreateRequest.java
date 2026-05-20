package kr.hs.dgsw.cyworldretro.domain.diary.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import kr.hs.dgsw.cyworldretro.domain.diary.Emotion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiaryCreateRequest {

    @NotBlank(message = "다이어리 제목은 필수 입력 값입니다.")
    @Size(max = 100, message = "다이어리 제목은 100자 이하로 입력해주세요.")
    private String title;

    @NotBlank(message = "다이어리 내용은 필수 입력 값입니다.")
    @Size(max = 3000, message = "다이어리 내용은 3000자 이하로 입력해주세요.")
    private String content;

    @NotNull(message = "감정 스티커는 필수 입력 값입니다.")
    private Emotion emotion;

    private boolean publicPost;
}
