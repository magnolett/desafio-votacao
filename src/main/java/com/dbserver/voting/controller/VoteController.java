package com.dbserver.voting.controller;

import com.dbserver.voting.model.dto.VoteCreatedDTO;
import com.dbserver.voting.model.dto.VoteDTO;
import com.dbserver.voting.service.VoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@CrossOrigin
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/vote")
public class VoteController {

    private final VoteService voteService;

    @PostMapping
    public ResponseEntity<VoteDTO> create(@Valid @RequestBody VoteCreatedDTO voteCreatedDTO) {
        VoteDTO vote = voteService.create(voteCreatedDTO);
        return new ResponseEntity<>(vote, CREATED);
    }

}