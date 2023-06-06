package com.dbserver.voting.service;

import com.dbserver.voting.model.dto.AgendaVotingStatusDTO;
import com.dbserver.voting.model.dto.VotingStatusDTO;
import com.dbserver.voting.model.entity.Agenda;
import com.dbserver.voting.model.entity.Vote;
import com.dbserver.voting.model.entity.VotingSession;
import com.dbserver.voting.model.enums.VotingStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class VotingStatusService {

    private final AgendaService agendaService;
    private final VotingSessionService votingSessionService;
    private final VoteService voteService;

    public AgendaVotingStatusDTO getAgendaVotingStatus(String idAgenda) {
        log.info("Starting voting status build for agenda id: {}", idAgenda);

        Agenda agenda = agendaService.findById(idAgenda);
        VotingSession voting = votingSessionService.findByIdAgenda(agenda.getId());
        List<Vote> votes = voteService.findAllByIdAgenda(agenda.getId());

        VotingStatusDTO votingStatusDTO = buildVotingStatusDTO(voting, votes);
        AgendaVotingStatusDTO agendaVotingStatusDTO = buildAgendaVotingStatusDTO(agenda, votingStatusDTO);

        log.info("Voting status build completed: {}", agendaVotingStatusDTO);
        return agendaVotingStatusDTO;
    }

    private AgendaVotingStatusDTO buildAgendaVotingStatusDTO(Agenda agenda, VotingStatusDTO votingStatusDTO) {
        return AgendaVotingStatusDTO.builder()
                .description(agenda.getDescription())
                .title(agenda.getTitle())
                .voting(votingStatusDTO)
                .build();
    }

    private VotingStatusDTO buildVotingStatusDTO(VotingSession voting, List<Vote> votes) {
        Integer votesAgainst = getVotesAgainst(votes);
        Integer votesInFavor = getVotesInFavor(votes);

        VotingStatus votingStatus;

        if (votingSessionService.isOpen(voting)) {
            votingStatus = VotingStatus.OPEN;
        } else {
            votingStatus = getVotingStatusByVotes(votesAgainst, votesInFavor);
        }

        return VotingStatusDTO.builder()
                .duration(voting.getDuration())
                .votingStatus(votingStatus)
                .startDate(voting.getStartDate())
                .endDate(voting.getEndDate())
                .totalVotes(votes.size())
                .votesAgainst(votesAgainst)
                .votesInFavor(votesInFavor)
                .build();
    }

    private VotingStatus getVotingStatusByVotes(Integer votesAgainst, Integer votesInFavor) {
        if (votesAgainst > votesInFavor) return VotingStatus.DISAPPROVED;
        if (votesInFavor > votesAgainst) return VotingStatus.APPROVED;
        else return VotingStatus.TIED;
    }

    private Integer getVotesInFavor(List<Vote> votes) {
        return Math.toIntExact(votes.stream().filter(Vote::getVote).count());
    }

    private Integer getVotesAgainst(List<Vote> votes) {
        return Math.toIntExact(votes.stream().filter(vote -> !vote.getVote()).count());
    }

}