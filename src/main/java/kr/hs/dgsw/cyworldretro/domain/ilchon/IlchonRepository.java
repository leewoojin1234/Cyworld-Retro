package kr.hs.dgsw.cyworldretro.domain.ilchon;

import kr.hs.dgsw.cyworldretro.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IlchonRepository extends JpaRepository<Ilchon, Long> {
    List<Ilchon> findAllByReceiverAndStatus(Member receiver, IlchonStatus status);
    List<Ilchon> findAllByRequesterAndStatus(Member requester, IlchonStatus status);
    Optional<Ilchon> findByRequesterAndReceiver(Member requester, Member receiver);

    @Query("""
            select i from Ilchon i
            where i.status = kr.hs.dgsw.cyworldretro.domain.ilchon.IlchonStatus.ACCEPTED
            and (i.requester = :member or i.receiver = :member)
            order by i.createdAt desc
            """)
    List<Ilchon> findAcceptedIlchons(Member member);
}
