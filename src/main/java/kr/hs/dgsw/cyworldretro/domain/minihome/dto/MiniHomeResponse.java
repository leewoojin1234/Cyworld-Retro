package kr.hs.dgsw.cyworldretro.domain.minihome.dto;

import kr.hs.dgsw.cyworldretro.domain.minihome.MiniHome;
import kr.hs.dgsw.cyworldretro.domain.minihome.SkinType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MiniHomeResponse {

    private Long id;
    private Long memberId;
    private String nickname;
    private String profileMessage;
    private SkinType skinType;
    private String bgmUrl;
    private int todayVisit;
    private int totalVisit;

    public static MiniHomeResponse from(MiniHome miniHome) {
        return MiniHomeResponse.builder()
                .id(miniHome.getId())
                .memberId(miniHome.getMember().getId())
                .nickname(miniHome.getMember().getNickname())
                .profileMessage(miniHome.getProfileMessage())
                .skinType(miniHome.getSkinType())
                .bgmUrl(miniHome.getBgmUrl())
                .todayVisit(miniHome.getTodayVisit())
                .totalVisit(miniHome.getTotalVisit())
                .build();
    }
}
