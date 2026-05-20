package kr.hs.dgsw.cyworldretro.domain.guestbook;

import kr.hs.dgsw.cyworldretro.domain.minihome.MiniHome;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestBookRepository extends JpaRepository<GuestBook, Long>, GuestBookRepositoryCustom {
    Page<GuestBook> findAllByMiniHome(MiniHome miniHome, Pageable pageable);
}
