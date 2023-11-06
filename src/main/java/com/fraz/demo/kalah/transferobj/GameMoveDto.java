package com.fraz.demo.kalah.transferobj;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record GameMoveDto(
    @NotBlank(message = "Player name must be provided") String userName,
    @NotNull(message = "House index must be provided")
        @PositiveOrZero(message = "House index must be positive or zero")
        Integer houseIndex) {}
