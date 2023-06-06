package com.dbserver.voting.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "cpf-validator", url = "${url.cpf_validator}", dismiss404 = true)
public interface CpfValidatorClient {
    @GetMapping("/v1/validator")
    ResponseEntity<ValidationDTO> validate(@RequestParam String token, @RequestParam String value, @RequestParam String type);
}