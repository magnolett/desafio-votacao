package com.dbserver.voting.repository;

import com.dbserver.voting.model.entity.VotingSession;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface VotingSessionRepository extends MongoRepository<VotingSession, String> {
    Optional<VotingSession> findByIdAgenda(String idAgenda);
    boolean existsByIdAgenda(String idAgenda);
}