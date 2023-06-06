package com.dbserver.voting.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.ZonedDateTime;

public record ApiExceptionSchema(String message, Integer status, String error, ZonedDateTime timestamp) {
}