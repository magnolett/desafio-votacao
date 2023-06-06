package com.dbserver.voting.controller;

import com.dbserver.voting.model.dto.VotingSessionCreateDTO;
import com.dbserver.voting.model.dto.VotingSessionDTO;
import com.dbserver.voting.service.VotingSessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@CrossOrigin
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/voting-session")
public class VotingSessionController {

    private final VotingSessionService votingSessionService;

    @PostMapping
    public ResponseEntity<VotingSessionDTO> create(@Valid @RequestBody VotingSessionCreateDTO agendaOpenVotingDTO) {
        VotingSessionDTO voting = votingSessionService.create(agendaOpenVotingDTO);
        return new ResponseEntity<>(voting, CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VotingSessionDTO> getById(@PathVariable String id) {
        VotingSessionDTO voting = votingSessionService.getById(id);
        return new ResponseEntity<>(voting, OK);
    }

}