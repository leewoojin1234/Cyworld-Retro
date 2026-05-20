package kr.hs.dgsw.cyworldretro.domain.minihome.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MiniHomeProfileUpdateRequest {

    @Size(max = 100, message = "상태 메시지는 100자 이하로 입력해주세요.")
    private String profileMessage;
}
