package kr.hs.dgsw.cyworldretro.domain.acorn.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AcornChargeRequest {

    @Min(value = 1, message = "도토리는 1개 이상 충전해야 합니다.")
    @Max(value = 1000, message = "도토리는 한 번에 1000개 이하로 충전할 수 있습니다.")
    private int amount;
}
