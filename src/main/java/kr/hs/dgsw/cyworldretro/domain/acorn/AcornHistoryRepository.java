package kr.hs.dgsw.cyworldretro.domain.acorn;

import kr.hs.dgsw.cyworldretro.domain.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AcornHistoryRepository extends JpaRepository<AcornHistory, Long> {
    Page<AcornHistory> findAllByMember(Member member, Pageable pageable);
}
