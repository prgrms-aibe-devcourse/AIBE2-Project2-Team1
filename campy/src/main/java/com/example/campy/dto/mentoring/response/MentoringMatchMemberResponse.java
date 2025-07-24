package com.example.campy.dto.mentoring.response;

import com.example.campy.constant.MatchRole;
import com.example.campy.entity.MentoringMatchMember;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class MentoringMatchMemberResponse {

    private Integer userId;
    private String userName;
    private MatchRole role;
    private LocalDateTime joinedAt;

    public static MentoringMatchMemberResponse from(MentoringMatchMember member){
        return MentoringMatchMemberResponse.builder()
                .userId(member.getUser().getUserId())
                // .userName(member.getUser().getName()) // 이름도 받을지는 보류
                .role(member.getRole())
                .joinedAt(member.getJoinedAt())
                .build();
    }

}
