package kr.hs.dgsw.cyworldretro.domain.diary;

import kr.hs.dgsw.cyworldretro.domain.minihome.MiniHome;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
    Page<Diary> findAllByMiniHome(MiniHome miniHome, Pageable pageable);
}
