package com.example.campy.repository;

import com.example.campy.constant.MatchRole;
import com.example.campy.entity.MentoringMatchMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MentoringMatchMemberRepository extends JpaRepository<MentoringMatchMember, Integer> {

    List<MentoringMatchMember> findByMentoringMatch_MatchId(Integer matchId);

    List<MentoringMatchMember> findByUser_UserId(Integer userId);

    List<MentoringMatchMember> findByMentoringMatch_MatchIdAndRole(Integer matchId, MatchRole role);
}
