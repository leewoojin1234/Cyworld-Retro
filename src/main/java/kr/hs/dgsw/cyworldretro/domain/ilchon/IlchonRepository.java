package kr.hs.dgsw.cyworldretro.domain.ilchon;

import kr.hs.dgsw.cyworldretro.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IlchonRepository extends JpaRepository<Ilchon, Long> {
    List<Ilchon> findAllByReceiverAndStatus(Member receiver, IlchonStatus status);
    List<Ilchon> findAllByRequesterAndStatus(Member requester, IlchonStatus status);
}
