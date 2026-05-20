package kr.hs.dgsw.cyworldretro.domain.guestbook;

import jakarta.persistence.*;
import kr.hs.dgsw.cyworldretro.domain.member.Member;
import kr.hs.dgsw.cyworldretro.domain.minihome.MiniHome;
import kr.hs.dgsw.cyworldretro.global.entity.BaseTimeEntity;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class GuestBook extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "minihome_id")
    private MiniHome miniHome;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private Member writer;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private boolean isSecret;

    public void update(String content, boolean isSecret) {
        this.content = content;
        this.isSecret = isSecret;
    }
}
