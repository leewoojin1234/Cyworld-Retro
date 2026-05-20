package kr.hs.dgsw.cyworldretro.domain.ilchon.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IlchonRequestCreateRequest {

    @NotNull(message = "일촌 신청 대상은 필수 입력 값입니다.")
    private Long receiverId;
}
