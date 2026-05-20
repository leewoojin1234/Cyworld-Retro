package kr.hs.dgsw.cyworldretro.domain.member;

import jakarta.persistence.*;
import kr.hs.dgsw.cyworldretro.global.exception.BusinessException;
import kr.hs.dgsw.cyworldretro.global.entity.BaseTimeEntity;
import lombok.*;
import org.springframework.http.HttpStatus;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Builder.Default
    private int acorn = 0;

    public void addAcorn(int amount) {
        this.acorn += amount;
    }

    public void useAcorn(int amount) {
        if (this.acorn < amount) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "도토리가 부족합니다.");
        }
        this.acorn -= amount;
    }
}
