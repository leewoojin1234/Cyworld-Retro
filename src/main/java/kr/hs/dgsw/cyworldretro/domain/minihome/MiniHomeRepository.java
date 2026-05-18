package kr.hs.dgsw.cyworldretro.domain.minihome;

import kr.hs.dgsw.cyworldretro.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MiniHomeRepository extends JpaRepository<MiniHome, Long> {
    Optional<MiniHome> findByMemberId(Long memberId);
    Optional<MiniHome> findByMember(Member member);
}
