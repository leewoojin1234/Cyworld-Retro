package kr.hs.dgsw.cyworldretro.domain.guestbook;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import java.util.List;

public class GuestBookRepositoryImpl implements GuestBookRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public GuestBookRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<GuestBook> searchByMiniHome(Long miniHomeId, String keyword, String viewerEmail, Pageable pageable) {
        QGuestBook guestBook = QGuestBook.guestBook;

        BooleanBuilder where = new BooleanBuilder()
                .and(guestBook.miniHome.id.eq(miniHomeId))
                .and(guestBook.isSecret.isFalse()
                        .or(guestBook.writer.email.eq(viewerEmail))
                        .or(guestBook.miniHome.member.email.eq(viewerEmail)));

        if (StringUtils.hasText(keyword)) {
            where.and(guestBook.content.containsIgnoreCase(keyword));
        }

        List<GuestBook> content = queryFactory
                .selectFrom(guestBook)
                .join(guestBook.writer).fetchJoin()
                .join(guestBook.miniHome).fetchJoin()
                .join(guestBook.miniHome.member).fetchJoin()
                .where(where)
                .orderBy(guestBook.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(guestBook.count())
                .from(guestBook)
                .where(where)
                .fetchOne();

        return new PageImpl<>(content, pageable, total == null ? 0 : total);
    }
}
