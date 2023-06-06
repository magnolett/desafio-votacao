package com.dbserver.voting.service;

import com.dbserver.voting.exception.BusinessException;
import com.dbserver.voting.exception.ConflictException;
import com.dbserver.voting.mapper.VoteMapper;
import com.dbserver.voting.model.dto.VoteCreatedDTO;
import com.dbserver.voting.model.dto.VoteDTO;
import com.dbserver.voting.model.entity.Vote;
import com.dbserver.voting.model.entity.VotingSession;
import com.dbserver.voting.repository.VoteRepository;
import com.mongodb.DuplicateKeyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class VoteService {

    private final VoteRepository voteRepository;
    private final VoteMapper voteMapper;
    private final VotingSessionService votingSessionService;
    private final CpfValidatorService cpfValidatorService;

    public VoteDTO create(VoteCreatedDTO voteCreatedDTO) {
        log.info("Starting vote creation: {}", voteCreatedDTO);

        VotingSession voting = votingSessionService.findByIdAgenda(voteCreatedDTO.getIdAgenda());
        if (votingSessionService.isOpen(voting)) {

            cpfValidatorService.validate(voteCreatedDTO.getCpf());
            Vote vote = voteMapper.toEntity(voteCreatedDTO);
            VoteDTO voteDTO = voteMapper.toDTO(this.save(vote));
            log.info("Vote created: {}", voteDTO);
            return voteDTO;

        } else {
            log.error("Voting has already ended");
            throw new ConflictException("Voting has already ended");
        }
    }

    private Vote save(Vote vote) {
        try {
            return voteRepository.save(vote);
        } catch (DuplicateKeyException e) {
            log.error("Error saving vote, user has already voted");
            throw new ConflictException("User has already voted");
        } catch (RuntimeException e) {
            log.error("Error saving vote", e);
            throw new BusinessException();
        }
    }

    public List<Vote> findAllByIdAgenda(String idVoting) {
        return voteRepository.findAllByIdAgenda(idVoting);
    }

}