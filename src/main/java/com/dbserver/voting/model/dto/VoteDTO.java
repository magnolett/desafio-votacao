package com.dbserver.voting.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class VoteDTO {

    private String idAgenda;
    private String cpf;
    private Boolean vote;
    private LocalDateTime createdDate;

}