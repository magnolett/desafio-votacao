package com.dbserver.voting.model.dto;

import com.dbserver.voting.model.enums.VotingStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class VotingStatusDTO {

    private VotingStatus votingStatus;
    private Long duration;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer totalVotes;
    private Integer votesAgainst;
    private Integer votesInFavor;

}