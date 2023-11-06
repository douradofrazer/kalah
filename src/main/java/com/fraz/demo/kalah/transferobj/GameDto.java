package com.fraz.demo.kalah.transferobj;

import java.util.UUID;

public record GameDto(UUID gameReference, String playerOne, String playerTwo) {}
