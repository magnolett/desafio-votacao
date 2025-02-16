package com.dbserver.voting.controller;

import com.dbserver.voting.model.dto.VotingSessionCreateDTO;
import com.dbserver.voting.model.entity.Agenda;
import com.dbserver.voting.model.entity.VotingSession;
import com.dbserver.voting.repository.AgendaRepository;
import com.dbserver.voting.repository.VotingSessionRepository;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations = "classpath:test.properties")
class VotingSessionControllerTest {

    @Autowired
    private AgendaRepository agendaRepository;
    @Autowired
    private VotingSessionRepository votingSessionRepository;

    @LocalServerPort
    private int port;

    private RequestSpecification requestSpec;

    @BeforeAll
    public void init() {
        String URL = "http://localhost:" + port + "/api/v1/voting-session";
        requestSpec = new RequestSpecBuilder()
                .setBaseUri(URL)
                .setContentType(ContentType.JSON)
                .build();
    }

    @Test
    void shouldCreateVotingAndStatus201() {
        Agenda agenda = agendaRepository.save(Agenda.builder().build());
        VotingSessionCreateDTO votingSessionCreateDTO = VotingSessionCreateDTO.builder()
                .idAgenda(agenda.getId())
                .duration(6000l)
                .build();
        given(requestSpec)
                .body(votingSessionCreateDTO)
                .post()
                .then()
                .statusCode(201)
                .body("idAgenda", equalTo(agenda.getId()));
    }

    @Test
    void shouldThrowBadRequestExceptionWithoutRequiredFieldAndStatus400() {
        VotingSessionCreateDTO votingSessionCreateDTO = VotingSessionCreateDTO.builder()
                .idAgenda(null)
                .duration(60000l)
                .build();
        given(requestSpec)
                .body(votingSessionCreateDTO)
                .post()
                .then()
                .statusCode(400);
    }

    @Test
    void shouldThrowNotFoundExceptionWithNonExistentAgendaAndStatus404() {
        VotingSessionCreateDTO votingSessionCreateDTO = VotingSessionCreateDTO.builder()
                .idAgenda("idAgenda")
                .duration(6000l)
                .build();
        given(requestSpec)
                .body(votingSessionCreateDTO)
                .post()
                .then()
                .statusCode(404)
                .body("message", containsString("Agenda not found"));
    }

    @Test
    void shouldGetVotingByIdAndStatus200() {
        VotingSession voting = votingSessionRepository.save(VotingSession.builder().idAgenda(UUID.randomUUID().toString()).build());
        given(requestSpec)
                .get("/" + voting.getId())
                .then()
                .statusCode(200);
    }

    @Test
    void shouldThrowNotFoundExceptionWithNonexistentVotingAndStatus404() {
        given(requestSpec)
                .get("/" + UUID.randomUUID())
                .then()
                .statusCode(404)
                .body("message", containsString("Voting session not found"));
    }

}