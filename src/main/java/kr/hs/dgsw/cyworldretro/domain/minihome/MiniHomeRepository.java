package kr.hs.dgsw.cyworldretro.domain.minihome;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MiniHomeRepository extends JpaRepository<MiniHome, Long> {
    Optional<MiniHome> findByMemberId(Long memberId);
}
