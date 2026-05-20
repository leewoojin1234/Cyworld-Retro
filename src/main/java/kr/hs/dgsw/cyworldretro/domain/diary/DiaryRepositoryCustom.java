package kr.hs.dgsw.cyworldretro.domain.diary;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DiaryRepositoryCustom {

    Page<Diary> searchByMiniHome(Long miniHomeId, String keyword, Emotion emotion, String viewerEmail, Pageable pageable);
}
