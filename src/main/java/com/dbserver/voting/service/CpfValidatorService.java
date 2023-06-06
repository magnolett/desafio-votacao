package com.dbserver.voting.service;

import com.dbserver.voting.client.CpfValidatorClient;
import com.dbserver.voting.client.ValidationDTO;
import com.dbserver.voting.exception.BusinessException;
import com.dbserver.voting.exception.UnableToVoteException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CpfValidatorService {

    @Value("${token")
    private String token;

    private String fieldType = "cpf";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final CpfValidatorClient cpfValidatorClient;

    public void validate(String cpf) {
        logger.info("Starting cpf validation: {}", cpf);

        ResponseEntity<ValidationDTO> validate = validateCpf(cpf);
        if (validate.getStatusCode().equals(NOT_FOUND)) {
            logger.error("Invalid cpf: {}", cpf);
            throw new UnableToVoteException();
        }

        logger.info("Valid cpf: {}", cpf);
    }

    private ResponseEntity<ValidationDTO> validateCpf(String cpf) {
        try {
            return this.cpfValidatorClient.validate(token, cpf, fieldType);
        } catch (RuntimeException e) {
            logger.error("Erro validating cpf: ", e);
            throw new BusinessException();
        }
    }

}