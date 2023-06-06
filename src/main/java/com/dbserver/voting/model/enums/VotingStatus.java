package com.dbserver.voting.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
@AllArgsConstructor
public enum VotingStatus {
    OPEN("open"),
    APPROVED("approved"),
    DISAPPROVED("disapproved"),
    TIED("tied");

    private final String id;

    public static Optional<VotingStatus> getById(String id) {
        return Arrays.stream(VotingStatus.values())
                .filter(status -> status.getId().equals(id.toLowerCase()))
                .findFirst();
    }
}
