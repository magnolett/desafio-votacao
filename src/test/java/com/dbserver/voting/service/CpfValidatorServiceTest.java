package com.dbserver.voting.service;

import com.dbserver.voting.client.CpfValidatorClient;
import com.dbserver.voting.client.ValidationDTO;
import com.dbserver.voting.exception.BusinessException;
import com.dbserver.voting.exception.UnableToVoteException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(MockitoExtension.class)
class CpfValidatorServiceTest {

    @Mock
    private CpfValidatorClient cpfValidatorClient;

    @InjectMocks
    private CpfValidatorService cpfValidatorService;

    private final String cpf = "09390712904";
    private final String token = "3743|Ts61zNPNRHKFXqzitsmZBRQSCcRsQxxU";
    private final String type = "cpf";

    @Test
    void shouldThrowBusinessException() {
        when(cpfValidatorClient.validate(token, cpf, type)).thenThrow(new RuntimeException("erro teste"));
        BusinessException throwable =
                catchThrowableOfType(() -> cpfValidatorService.validate(cpf), BusinessException.class);
        assertThat(throwable.getClass(), equalTo(BusinessException.class));
    }

}