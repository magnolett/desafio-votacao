package com.dbserver.voting.service;

import com.dbserver.voting.exception.BusinessException;
import com.dbserver.voting.exception.EntityNotFoundException;
import com.dbserver.voting.mapper.AgendaMapper;
import com.dbserver.voting.model.dto.AgendaCreateDTO;
import com.dbserver.voting.model.dto.AgendaDTO;
import com.dbserver.voting.model.entity.Agenda;
import com.dbserver.voting.repository.AgendaRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AgendaService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final AgendaRepository agendaRepository;
    private final AgendaMapper agendaMapper;

    public AgendaDTO create(AgendaCreateDTO agendaCreateDTO) {
        logger.info("Starting agenda creation: {}", agendaCreateDTO);
        Agenda agenda = agendaMapper.toEntity(agendaCreateDTO);
        AgendaDTO agendaDTO = agendaMapper.toDTO(this.save(agenda));
        logger.info("Agenda created: {}", agendaDTO);
        return agendaDTO;
    }

    private Agenda save(Agenda agenda) {
        try {
            return agendaRepository.save(agenda);
        } catch (RuntimeException e) {
            logger.error("Error saving agenda: ", e);
            throw new BusinessException();
        }
    }

    public AgendaDTO getAgendaById(String id) {
        logger.info("Starting agenda search: {}", id);
        AgendaDTO agendaDTO = agendaMapper.toDTO(this.findById(id));
        logger.info("Agenda found: {}", agendaDTO);
        return agendaDTO;
    }

    public Agenda findById(String id) {
        return agendaRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Agenda not found: {}", id);
                    throw new EntityNotFoundException("Agenda not found for id: " + id);
                });
    }

    public List<AgendaDTO> getAll() {
        return agendaRepository.findAll().stream()
                .map(agenda -> agendaMapper.toDTO(agenda))
                .toList();
    }

}