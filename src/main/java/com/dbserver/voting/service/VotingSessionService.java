package com.dbserver.voting.service;

import com.dbserver.voting.exception.BusinessException;
import com.dbserver.voting.exception.ConflictException;
import com.dbserver.voting.exception.EntityNotFoundException;
import com.dbserver.voting.mapper.VotingSessionMapper;
import com.dbserver.voting.model.dto.VotingSessionCreateDTO;
import com.dbserver.voting.model.dto.VotingSessionDTO;
import com.dbserver.voting.model.entity.VotingSession;
import com.dbserver.voting.repository.VotingSessionRepository;
import com.mongodb.DuplicateKeyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class VotingSessionService {


    private final VotingSessionRepository votingSessionRepository;
    private final VotingSessionMapper votingSessionMapper;
    private final AgendaService agendaService;

    public VotingSessionDTO create(VotingSessionCreateDTO votingSessionCreateDTO) {
        log.info("Starting voting session creation: {}", votingSessionCreateDTO);
        agendaService.findById(votingSessionCreateDTO.getIdAgenda());
        VotingSession voting = votingSessionMapper.toEntity(votingSessionCreateDTO);
        VotingSessionDTO votingSessionDTO = votingSessionMapper.toDTO(this.save(voting));
        log.info("Voting session created: {}", voting);
        return votingSessionDTO;
    }

    private VotingSession save(VotingSession voting) {
        try {
            return votingSessionRepository.save(voting);
        } catch (DuplicateKeyException e) {
            log.error("Error saving voting session. Voting already started");
            throw new ConflictException("Voting session already started");
        } catch (RuntimeException e) {
            log.error("Error saving voting session", e);
            throw new BusinessException();
        }
    }

    public VotingSessionDTO getById(String id) {
        log.info("Starting voting session search: {}", id);
        VotingSessionDTO votingSessionDTO = votingSessionMapper.toDTO(this.findById(id));
        log.info("Voting session found: {}", votingSessionDTO);
        return votingSessionDTO;
    }

    public VotingSession findById(String id) {
        return votingSessionRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Voting session not found: {}", id);
                    throw new EntityNotFoundException("Voting session not found: " + id);
                });
    }

    public VotingSession findByIdAgenda(String idAgenda) {
        return votingSessionRepository.findByIdAgenda(idAgenda)
                .orElseThrow(() -> {
                    log.error("Voting session not found for agenda: {}", idAgenda);
                    throw new EntityNotFoundException("Voting session not found for agenda: " + idAgenda);
                });
    }

    public boolean isOpen(VotingSession voting) {
        LocalDateTime endDate = voting.getEndDate();
        LocalDateTime now = LocalDateTime.now();
        return now.isBefore(endDate);
    }

}