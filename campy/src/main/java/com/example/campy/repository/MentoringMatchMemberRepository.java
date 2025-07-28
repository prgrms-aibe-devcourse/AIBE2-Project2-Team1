package com.example.campy.repository;

import com.example.campy.constant.MatchRole;
import com.example.campy.entity.MentoringMatchMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface MentoringMatchMemberRepository extends JpaRepository<MentoringMatchMember, Integer> {

    List<MentoringMatchMember> findByMentoringMatch_MatchId(Integer matchId);

    List<MentoringMatchMember> findByUser_UserId(Integer userId);

    List<MentoringMatchMember> findByMentoringMatch_MatchIdAndRole(Integer matchId, MatchRole role);

    List<MentoringMatchMember> findByUser_UserIdAndRole(Integer userId, MatchRole role);

    @Query("SELECT m.mentoringMatch.matchId FROM MentoringMatchMember m WHERE m.user.userId = :userId AND m.role = :role")
    List<Integer> findMatchIdsByUserIdAndRole(@Param("userId") Integer userId, @Param("role") MatchRole role);

}
