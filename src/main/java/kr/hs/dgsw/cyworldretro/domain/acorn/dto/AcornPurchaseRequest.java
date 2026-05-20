package kr.hs.dgsw.cyworldretro.domain.acorn.dto;

import jakarta.validation.constraints.NotNull;
import kr.hs.dgsw.cyworldretro.domain.acorn.ShopItemType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AcornPurchaseRequest {

    @NotNull(message = "구매할 아이템은 필수 입력 값입니다.")
    private ShopItemType itemType;
}
