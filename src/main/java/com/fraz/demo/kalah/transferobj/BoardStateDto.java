package com.fraz.demo.kalah.transferobj;

import java.util.List;

public record BoardStateDto(
    String gameRef, String status, String currentPlayer, List<PitDto> pits, String winner) {}
