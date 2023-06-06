package com.dbserver.voting.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class VotingSessionDTO {

    private String idAgenda;
    private Long duration;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

}