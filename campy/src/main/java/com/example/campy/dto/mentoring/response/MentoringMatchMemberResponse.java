package com.example.campy.dto.mentoring.response;

import com.example.campy.constant.MatchRole;
import com.example.campy.entity.MentoringMatchMember;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class MentoringMatchMemberResponse {

    private Integer matchId;
    private Integer userId;
    private String userName;
    private MatchRole role;
    private LocalDateTime joinedAt;

    public static MentoringMatchMemberResponse from(MentoringMatchMember entity) {
        return MentoringMatchMemberResponse.builder()
                .matchId(entity.getMentoringMatch().getMatchId())
                .userId(entity.getUser().getUserId())
               // .userName(entity.getUser().getName()) // 추후 논의해서 추가
                .role(entity.getRole())
                .joinedAt(entity.getJoinedAt())
                .build();
    }

}
