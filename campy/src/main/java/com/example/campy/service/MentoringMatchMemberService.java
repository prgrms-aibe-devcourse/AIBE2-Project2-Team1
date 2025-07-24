package com.example.campy.service;

import com.example.campy.constant.ErrorCode;
import com.example.campy.constant.MatchRole;
import com.example.campy.dto.mentoring.request.MentoringMatchMemberCreateRequest;
import com.example.campy.dto.mentoring.response.MentoringMatchMemberResponse;
import com.example.campy.dto.mentoring.response.MentoringMatchResponse;
import com.example.campy.entity.MentoringMatch;
import com.example.campy.entity.MentoringMatchMember;
import com.example.campy.entity.User;
import com.example.campy.exception.GeneralException;
import com.example.campy.repository.MentoringMatchMemberRepository;
import com.example.campy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MentoringMatchMemberService {

    private final MentoringMatchMemberRepository memberRepo;
    private final UserRepository userRepo;

    public void createMembers(MentoringMatch match, List<MentoringMatchMemberCreateRequest> reqs){

        for (MentoringMatchMemberCreateRequest req: reqs){
            User user = userRepo.findById(req.getUserId())
                    .orElseThrow(() -> new GeneralException(ErrorCode.NOT_FOUND, "사용자가 존재하지 않습니다."));

            MentoringMatchMember member = MentoringMatchMember.builder()
                    .mentoringMatch(match)
                    .user(user)
                    .role(req.getRole())
                    .joinedAt(LocalDateTime.now())
                    .build();

            memberRepo.save(member);
        }

    }

    // 전체 조회
    public List<MentoringMatchMemberResponse> findAll(){
        return memberRepo.findAll().stream()
                .map(MentoringMatchMemberResponse::from)
                .toList();
    }

    // 매칭 ID 조회
    public List<MentoringMatchMemberResponse> findByMatchId(Integer matchId){
        return memberRepo.findByMentoringMatch_MatchId(matchId).stream()
                .map(MentoringMatchMemberResponse::from)
                .toList();
    }

    // 사용자 ID 조회
    public List<MentoringMatchMemberResponse> findByUserId(Integer userId){
        return memberRepo.findByUser_UserId(userId).stream()
                .map(MentoringMatchMemberResponse::from)
                .toList();
    }

    // 매칭 ID + 역할로 조회
    public List<MentoringMatchMemberResponse> findByMatchIdAndRole(Integer matchId, MatchRole role){
        return memberRepo.findByMentoringMatch_MatchIdAndRole(matchId, role).stream()
                .map(MentoringMatchMemberResponse::from)
                .toList();
    }

    public void delete(Integer memberId) {
        if (!memberRepo.existsById(memberId)) {
            throw new GeneralException(ErrorCode.NOT_FOUND, "매칭 멤버가 존재하지 않습니다.");
        }
        memberRepo.deleteById(memberId);
    }

}
