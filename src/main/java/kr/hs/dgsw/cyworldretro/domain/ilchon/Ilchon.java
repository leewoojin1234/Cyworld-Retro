package kr.hs.dgsw.cyworldretro.domain.ilchon;

import jakarta.persistence.*;
import kr.hs.dgsw.cyworldretro.domain.member.Member;
import kr.hs.dgsw.cyworldretro.global.entity.BaseTimeEntity;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Ilchon extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id")
    private Member requester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private Member receiver;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private IlchonStatus status = IlchonStatus.PENDING;

    private String ilchonName; // 수락 후 서로에게 부여하는 명칭

    public void accept(String ilchonName) {
        this.status = IlchonStatus.ACCEPTED;
        this.ilchonName = ilchonName;
    }

    public void reject() {
        this.status = IlchonStatus.REJECTED;
    }
}
