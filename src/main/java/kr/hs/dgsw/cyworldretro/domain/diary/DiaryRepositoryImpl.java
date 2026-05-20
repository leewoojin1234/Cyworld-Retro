package kr.hs.dgsw.cyworldretro.domain.diary;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import java.util.List;

public class DiaryRepositoryImpl implements DiaryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public DiaryRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<Diary> searchByMiniHome(Long miniHomeId, String keyword, Emotion emotion, String viewerEmail, Pageable pageable) {
        QDiary diary = QDiary.diary;

        BooleanBuilder where = new BooleanBuilder()
                .and(diary.miniHome.id.eq(miniHomeId))
                .and(diary.isPublic.isTrue()
                        .or(diary.miniHome.member.email.eq(viewerEmail)));

        if (StringUtils.hasText(keyword)) {
            where.and(diary.title.containsIgnoreCase(keyword)
                    .or(diary.content.containsIgnoreCase(keyword)));
        }

        if (emotion != null) {
            where.and(diary.emotion.eq(emotion));
        }

        List<Diary> content = queryFactory
                .selectFrom(diary)
                .join(diary.miniHome).fetchJoin()
                .join(diary.miniHome.member).fetchJoin()
                .where(where)
                .orderBy(diary.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(diary.count())
                .from(diary)
                .where(where)
                .fetchOne();

        return new PageImpl<>(content, pageable, total == null ? 0 : total);
    }
}
