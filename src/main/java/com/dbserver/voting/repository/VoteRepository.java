package com.dbserver.voting.repository;

import com.dbserver.voting.model.entity.Vote;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface VoteRepository extends MongoRepository<Vote, String> {
    List<Vote> findAllByIdAgenda(String idAgenda);
}