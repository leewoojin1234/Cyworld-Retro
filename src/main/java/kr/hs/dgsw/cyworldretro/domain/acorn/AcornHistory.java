package kr.hs.dgsw.cyworldretro.domain.acorn;

import jakarta.persistence.*;
import kr.hs.dgsw.cyworldretro.domain.member.Member;
import kr.hs.dgsw.cyworldretro.global.entity.BaseTimeEntity;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class AcornHistory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private int amount;

    @Enumerated(EnumType.STRING)
    private AcornType type;

    private String description;
}
