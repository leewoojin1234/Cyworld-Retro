package kr.hs.dgsw.cyworldretro.domain.diary.dto;

import kr.hs.dgsw.cyworldretro.domain.diary.Diary;
import kr.hs.dgsw.cyworldretro.domain.diary.Emotion;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class DiaryResponse {

    private Long id;
    private Long miniHomeId;
    private String title;
    private String content;
    private Emotion emotion;
    private boolean publicPost;
    private LocalDateTime createdAt;

    public static DiaryResponse from(Diary diary) {
        return DiaryResponse.builder()
                .id(diary.getId())
                .miniHomeId(diary.getMiniHome().getId())
                .title(diary.getTitle())
                .content(diary.getContent())
                .emotion(diary.getEmotion())
                .publicPost(diary.isPublic())
                .createdAt(diary.getCreatedAt())
                .build();
    }
}
