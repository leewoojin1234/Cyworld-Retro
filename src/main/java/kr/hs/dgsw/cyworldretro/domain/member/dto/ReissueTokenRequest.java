package kr.hs.dgsw.cyworldretro.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReissueTokenRequest {

    @NotBlank(message = "Refresh Token은 필수 입력 값입니다.")
    private String refreshToken;
}
