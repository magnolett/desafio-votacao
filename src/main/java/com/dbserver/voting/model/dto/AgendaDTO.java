package com.dbserver.voting.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AgendaDTO {

    private String title;
    private String description;
    private LocalDateTime createdDate;

}