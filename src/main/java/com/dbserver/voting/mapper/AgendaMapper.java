package com.dbserver.voting.mapper;

import com.dbserver.voting.exception.BusinessException;
import com.dbserver.voting.model.dto.AgendaCreateDTO;
import com.dbserver.voting.model.dto.AgendaDTO;
import com.dbserver.voting.model.entity.Agenda;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AgendaMapper {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ObjectMapper objectMapper;

    public Agenda toEntity(AgendaCreateDTO agendaCreateDTO) {
        try {
            return objectMapper.convertValue(agendaCreateDTO, Agenda.class);
        } catch (RuntimeException e) {
            logger.error("Error mapping agenda", e);
            throw new BusinessException();
        }
    }

    public AgendaDTO toDTO(Agenda agenda) {
        try {
            return objectMapper.convertValue(agenda, AgendaDTO.class);
        } catch (RuntimeException e) {
            logger.error("Error mapping agenda", e);
            throw new BusinessException();
        }
    }

}