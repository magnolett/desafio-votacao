package com.dbserver.voting.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AgendaVotingStatusDTO {

    private String title;
    private String description;
    private VotingStatusDTO voting;

}