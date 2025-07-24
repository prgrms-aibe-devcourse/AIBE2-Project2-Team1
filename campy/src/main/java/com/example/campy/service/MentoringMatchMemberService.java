package com.example.campy.service;

import com.example.campy.constant.ErrorCode;
import com.example.campy.dto.mentoring.request.MentoringMatchMemberCreateRequest;
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

}
