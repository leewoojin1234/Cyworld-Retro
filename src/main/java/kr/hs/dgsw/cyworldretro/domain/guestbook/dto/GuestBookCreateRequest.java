package kr.hs.dgsw.cyworldretro.domain.guestbook.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuestBookCreateRequest {

    @NotBlank(message = "방명록 내용은 필수 입력 값입니다.")
    @Size(max = 500, message = "방명록 내용은 500자 이하로 입력해주세요.")
    private String content;

    private boolean secret;
}
