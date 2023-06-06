package com.dbserver.voting.repository;

import com.dbserver.voting.model.entity.Agenda;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AgendaRepository extends MongoRepository<Agenda, String> {

}