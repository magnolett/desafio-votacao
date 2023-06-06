package com.dbserver.voting.mapper;

import com.dbserver.voting.exception.BusinessException;
import com.dbserver.voting.model.dto.VoteCreatedDTO;
import com.dbserver.voting.model.dto.VoteDTO;
import com.dbserver.voting.model.entity.Vote;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VoteMapper {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ObjectMapper objectMapper;

    public Vote toEntity(VoteCreatedDTO voteCreatedDTO) {
        try {
            return objectMapper.convertValue(voteCreatedDTO, Vote.class);
        } catch (RuntimeException e) {
            logger.error("Error mapping vote", e);
            throw new BusinessException();
        }
    }

    public VoteDTO toDTO(Vote vote) {
        try {
            return objectMapper.convertValue(vote, VoteDTO.class);
        } catch (RuntimeException e) {
            logger.error("Error mapping vote", e);
            throw new BusinessException();
        }
    }

}