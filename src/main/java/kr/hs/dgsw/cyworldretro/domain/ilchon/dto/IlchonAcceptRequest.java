package kr.hs.dgsw.cyworldretro.domain.ilchon.dto;

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
public class IlchonAcceptRequest {

    @NotBlank(message = "일촌명은 필수 입력 값입니다.")
    @Size(max = 20, message = "일촌명은 20자 이하로 입력해주세요.")
    private String ilchonName;
}
