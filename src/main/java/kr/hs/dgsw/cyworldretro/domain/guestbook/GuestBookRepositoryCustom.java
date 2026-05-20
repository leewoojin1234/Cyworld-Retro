package kr.hs.dgsw.cyworldretro.domain.guestbook;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GuestBookRepositoryCustom {

    Page<GuestBook> searchByMiniHome(Long miniHomeId, String keyword, String viewerEmail, Pageable pageable);
}
