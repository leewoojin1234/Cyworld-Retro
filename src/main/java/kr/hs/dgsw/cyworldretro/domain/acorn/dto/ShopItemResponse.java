package kr.hs.dgsw.cyworldretro.domain.acorn.dto;

import kr.hs.dgsw.cyworldretro.domain.acorn.ShopItemType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ShopItemResponse {

    private ShopItemType itemType;
    private String displayName;
    private int price;

    public static ShopItemResponse from(ShopItemType itemType) {
        return ShopItemResponse.builder()
                .itemType(itemType)
                .displayName(itemType.getDisplayName())
                .price(itemType.getPrice())
                .build();
    }
}
