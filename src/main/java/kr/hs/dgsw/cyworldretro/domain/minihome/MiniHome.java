package kr.hs.dgsw.cyworldretro.domain.minihome;

import jakarta.persistence.*;
import kr.hs.dgsw.cyworldretro.domain.member.Member;
import kr.hs.dgsw.cyworldretro.global.entity.BaseTimeEntity;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MiniHome extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String profileMessage;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private SkinType skinType = SkinType.BASIC;

    private String bgmUrl;

    @Builder.Default
    private int totalVisit = 0;

    @Builder.Default
    private int todayVisit = 0;

    public void updateProfile(String message) {
        this.profileMessage = message;
    }

    public void updateSkin(SkinType skinType) {
        this.skinType = skinType;
    }

    public void updateBgm(String bgmUrl) {
        this.bgmUrl = bgmUrl;
    }

    public void incrementVisit(int today, int total) {
        this.todayVisit = today;
        this.totalVisit = total;
    }
}
