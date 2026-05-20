package kr.hs.dgsw.cyworldretro.domain.minihome;

import kr.hs.dgsw.cyworldretro.domain.member.Member;
import kr.hs.dgsw.cyworldretro.domain.member.MemberRepository;
import kr.hs.dgsw.cyworldretro.domain.minihome.dto.MiniHomeProfileUpdateRequest;
import kr.hs.dgsw.cyworldretro.domain.minihome.dto.MiniHomeResponse;
import kr.hs.dgsw.cyworldretro.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MiniHomeService {

    private final MiniHomeRepository miniHomeRepository;
    private final MemberRepository memberRepository;
    private final RedisTemplate<String, String> redisTemplate;

    public MiniHomeResponse getMyMiniHome(String email) {
        Member member = getMember(email);
        MiniHome miniHome = miniHomeRepository.findByMember(member)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "미니홈피를 찾을 수 없습니다."));
        return MiniHomeResponse.from(miniHome);
    }

    public MiniHomeResponse getMiniHome(Long miniHomeId) {
        MiniHome miniHome = getMiniHomeEntity(miniHomeId);
        return MiniHomeResponse.from(miniHome);
    }

    @Transactional
    public MiniHomeResponse updateProfile(MiniHomeProfileUpdateRequest request, String email) {
        Member member = getMember(email);
        MiniHome miniHome = miniHomeRepository.findByMember(member)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "미니홈피를 찾을 수 없습니다."));

        miniHome.updateProfile(request.getProfileMessage());
        return MiniHomeResponse.from(miniHome);
    }

    @Transactional
    public MiniHomeResponse visit(Long miniHomeId) {
        MiniHome miniHome = getMiniHomeEntity(miniHomeId);
        Long todayVisit = redisTemplate.opsForValue().increment(createTodayVisitKey(miniHomeId));
        int nextTodayVisit = todayVisit == null ? miniHome.getTodayVisit() + 1 : todayVisit.intValue();
        miniHome.incrementVisit(nextTodayVisit, miniHome.getTotalVisit() + 1);
        return MiniHomeResponse.from(miniHome);
    }

    private MiniHome getMiniHomeEntity(Long miniHomeId) {
        return miniHomeRepository.findById(miniHomeId)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "미니홈피를 찾을 수 없습니다."));
    }

    private Member getMember(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
    }

    private String createTodayVisitKey(Long miniHomeId) {
        return "minihome:" + miniHomeId + ":today:" + LocalDate.now();
    }
}
