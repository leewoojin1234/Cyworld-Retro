package kr.hs.dgsw.cyworldretro.domain.ilchon;

import kr.hs.dgsw.cyworldretro.domain.ilchon.dto.IlchonAcceptRequest;
import kr.hs.dgsw.cyworldretro.domain.ilchon.dto.IlchonRequestCreateRequest;
import kr.hs.dgsw.cyworldretro.domain.ilchon.dto.IlchonResponse;
import kr.hs.dgsw.cyworldretro.domain.member.Member;
import kr.hs.dgsw.cyworldretro.domain.member.MemberRepository;
import kr.hs.dgsw.cyworldretro.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IlchonService {

    private final IlchonRepository ilchonRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public IlchonResponse request(IlchonRequestCreateRequest request, String requesterEmail) {
        Member requester = getMember(requesterEmail);
        Member receiver = memberRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "일촌 신청 대상을 찾을 수 없습니다."));

        if (requester.getId().equals(receiver.getId())) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "자기 자신에게 일촌 신청을 할 수 없습니다.");
        }

        if (existsRelation(requester, receiver)) {
            throw new BusinessException(HttpStatus.CONFLICT, "이미 일촌 신청 또는 관계가 존재합니다.");
        }

        Ilchon ilchon = Ilchon.builder()
                .requester(requester)
                .receiver(receiver)
                .build();

        return IlchonResponse.from(ilchonRepository.save(ilchon));
    }

    @Transactional
    public IlchonResponse accept(Long ilchonId, IlchonAcceptRequest request, String receiverEmail) {
        Ilchon ilchon = getIlchon(ilchonId);
        validateReceiver(ilchon, receiverEmail, "일촌 신청 수락 권한이 없습니다.");
        validatePending(ilchon);

        ilchon.accept(request.getIlchonName());
        return IlchonResponse.from(ilchon);
    }

    @Transactional
    public IlchonResponse reject(Long ilchonId, String receiverEmail) {
        Ilchon ilchon = getIlchon(ilchonId);
        validateReceiver(ilchon, receiverEmail, "일촌 신청 거절 권한이 없습니다.");
        validatePending(ilchon);

        ilchon.reject();
        return IlchonResponse.from(ilchon);
    }

    public List<IlchonResponse> getReceivedRequests(String receiverEmail) {
        Member receiver = getMember(receiverEmail);
        return ilchonRepository.findAllByReceiverAndStatus(receiver, IlchonStatus.PENDING)
                .stream()
                .map(IlchonResponse::from)
                .toList();
    }

    public List<IlchonResponse> getSentRequests(String requesterEmail) {
        Member requester = getMember(requesterEmail);
        return ilchonRepository.findAllByRequesterAndStatus(requester, IlchonStatus.PENDING)
                .stream()
                .map(IlchonResponse::from)
                .toList();
    }

    public List<IlchonResponse> getAcceptedIlchons(String email) {
        Member member = getMember(email);
        return ilchonRepository.findAcceptedIlchons(member)
                .stream()
                .map(IlchonResponse::from)
                .toList();
    }

    private boolean existsRelation(Member requester, Member receiver) {
        return ilchonRepository.findByRequesterAndReceiver(requester, receiver).isPresent()
                || ilchonRepository.findByRequesterAndReceiver(receiver, requester).isPresent();
    }

    private Ilchon getIlchon(Long ilchonId) {
        return ilchonRepository.findById(ilchonId)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "일촌 신청을 찾을 수 없습니다."));
    }

    private Member getMember(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
    }

    private void validateReceiver(Ilchon ilchon, String email, String message) {
        if (!ilchon.getReceiver().getEmail().equals(email)) {
            throw new BusinessException(HttpStatus.FORBIDDEN, message);
        }
    }

    private void validatePending(Ilchon ilchon) {
        if (ilchon.getStatus() != IlchonStatus.PENDING) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "대기 중인 일촌 신청만 처리할 수 있습니다.");
        }
    }
}
