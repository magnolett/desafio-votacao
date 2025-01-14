package com.dbserver.voting.model.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@Document(collection = "voting_session")
public class VotingSession {

    @Id
    private String id;
    @Indexed(unique = true)
    private String idAgenda;
    private Long duration;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

}