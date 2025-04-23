package com.gmail.drack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gmail.drack.models.PollChoiceVoted;
import com.gmail.drack.repository.projection.VotedUserProjection;

@Repository
public interface PollChoiceVotedRepository extends JpaRepository<PollChoiceVoted, Long> {

    @Query("SELECT poolChoice.votedUserId as id FROM PollChoiceVoted poolChoice WHERE poolChoice.pollChoiceId = :pollChoiceId")
    List<VotedUserProjection> getVotedUserIds(@Param("pollChoiceId") Long pollChoiceId);

    @Query("""
            SELECT CASE WHEN count(poolChoice) > 0 THEN true ELSE false END
            FROM PollChoiceVoted poolChoice
            WHERE poolChoice.votedUserId = :votedUserId
            AND poolChoice.pollChoiceId = :pollChoiceId
            """)
    boolean ifUserVoted(@Param("votedUserId") Long votedUserId, @Param("pollChoiceId") Long pollChoiceId);
}
