package kr.hs.dgsw.cyworldretro.domain.acorn;

public enum ShopItemType {
    SKIN_SKY("파도타기 스킨", 20, "SKY"),
    SKIN_PINK("핑크 미니룸 스킨", 20, "PINK"),
    BGM_SNOW_FLOWER("눈의 꽃 BGM", 15, "/bgm/snow-flower.mp3"),
    BGM_MEMORY("추억 여행 BGM", 15, "/bgm/memory.mp3");

    private final String displayName;
    private final int price;
    private final String value;

    ShopItemType(String displayName, int price, String value) {
        this.displayName = displayName;
        this.price = price;
        this.value = value;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getPrice() {
        return price;
    }

    public String getValue() {
        return value;
    }

    public boolean isSkin() {
        return name().startsWith("SKIN_");
    }

    public boolean isBgm() {
        return name().startsWith("BGM_");
    }
}
