package com.fraz.demo.kalah.transferobj;

import jakarta.validation.constraints.NotBlank;

public record PlayerDto(@NotBlank(message = "Username cannot be blank") String userName) {}
