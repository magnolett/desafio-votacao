package com.dbserver.voting.controller;

import com.dbserver.voting.model.dto.AgendaCreateDTO;
import com.dbserver.voting.model.entity.Agenda;
import com.dbserver.voting.model.entity.Vote;
import com.dbserver.voting.model.entity.VotingSession;
import com.dbserver.voting.repository.AgendaRepository;
import com.dbserver.voting.repository.VoteRepository;
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

import java.time.LocalDateTime;
import java.util.UUID;

import static com.dbserver.voting.model.enums.VotingStatus.APPROVED;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations = "classpath:test.properties")
class AgendaControllerTest {

    @Autowired
    private AgendaRepository agendaRepository;
    @Autowired
    private VotingSessionRepository votingSessionRepository;
    @Autowired
    private VoteRepository voteRepository;

    @LocalServerPort
    private int port;

    private RequestSpecification requestSpec;

    @BeforeAll
    public void init() {
        String URL = "http://localhost:" + port + "/api/v1/agenda";
        requestSpec = new RequestSpecBuilder()
                .setBaseUri(URL)
                .setContentType(ContentType.JSON)
                .build();
    }

    @Test
    void shouldCreateAgendaAndStatus201() {
        AgendaCreateDTO agendaCreateDTO = AgendaCreateDTO.builder()
                .title("teste")
                .description("teste")
                .build();
        given(requestSpec)
                .body(agendaCreateDTO)
                .post()
                .then()
                .statusCode(201)
                .body("title", equalTo(agendaCreateDTO.getTitle()))
                .body("description", equalTo(agendaCreateDTO.getDescription()));
    }

    @Test
    void shouldThrowBadRequestExceptionWithoutRequiredFieldAndStatus400() {
        AgendaCreateDTO agendaCreateDTO = AgendaCreateDTO.builder()
                .title("teste")
                .description(null)
                .build();
        given(requestSpec)
                .body(agendaCreateDTO)
                .post()
                .then()
                .statusCode(400);
    }

    @Test
    void shouldGetAgendaByIdAndStatus200() {
        Agenda agenda = agendaRepository.save(Agenda.builder().title("teste").description("teste").build());
        given(requestSpec)
                .get("/" + agenda.getId())
                .then()
                .statusCode(200)
                .body("title", equalTo(agenda.getTitle()))
                .body("description", equalTo(agenda.getDescription()));
    }

    @Test
    void shouldThrowNotFoundExceptionWithNonexistentAngedaAndStatus404() {
        given(requestSpec)
                .get("/" + UUID.randomUUID())
                .then()
                .statusCode(404)
                .body("message", containsString("Agenda not found"));
    }

    @Test
    void shouldGetAllAgendasAndStatus200() {
        given(requestSpec).get()
                .then()
                .statusCode(200);
    }

    @Test
    void shouldGetAgendaVotingStatusAndStatusCode200() {
        Agenda agenda = agendaRepository.save(Agenda.builder().title("teste").description("teste").build());

        VotingSession voting = votingSessionRepository.save(
                VotingSession.builder()
                        .idAgenda(agenda.getId())
                        .startDate(LocalDateTime.now())
                        .endDate(LocalDateTime.now())
                        .build()
        );

        voteRepository.save(Vote.builder().cpf(UUID.randomUUID().toString()).idAgenda(agenda.getId()).vote(true).build());
        voteRepository.save(Vote.builder().cpf(UUID.randomUUID().toString()).idAgenda(agenda.getId()).vote(true).build());
        voteRepository.save(Vote.builder().cpf(UUID.randomUUID().toString()).idAgenda(agenda.getId()).vote(true).build());

        given(requestSpec)
                .get("/" + agenda.getId() + "/voting/status")
                .then()
                .statusCode(200)
                .body("title", equalTo(agenda.getTitle()))
                .body("description", equalTo(agenda.getDescription()))
                .body("voting.votingStatus", equalTo(APPROVED.toString()))
                .body("voting.votesAgainst", equalTo(0))
                .body("voting.votesInFavor", equalTo(3));

    }

    @Test
    void shouldThrowNotFoundExceptionWithNonexistentAgendaAndStatus404() {
        given(requestSpec)
                .get("/" + UUID.randomUUID() + "/voting/status")
                .then()
                .statusCode(404)
                .body("message", containsString("Agenda not found"));

    }

    @Test
    void shouldThrowNotFoundExceptionWithNonexistentVotingAndStatus404() {
        Agenda agenda = agendaRepository.save(Agenda.builder().title("teste").description("teste").build());
        given(requestSpec)
                .get("/" + agenda.getId() + "/voting/status")
                .then()
                .statusCode(404)
                .body("message", equalTo("Voting session not found for agenda: " + agenda.getId()));
    }

}