package kr.hs.dgsw.cyworldretro.domain.diary;

import jakarta.persistence.*;
import kr.hs.dgsw.cyworldretro.domain.minihome.MiniHome;
import kr.hs.dgsw.cyworldretro.global.entity.BaseTimeEntity;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Diary extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "minihome_id")
    private MiniHome miniHome;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    private Emotion emotion;

    private boolean isPublic;

    public void update(String title, String content, Emotion emotion, boolean isPublic) {
        this.title = title;
        this.content = content;
        this.emotion = emotion;
        this.isPublic = isPublic;
    }
}
