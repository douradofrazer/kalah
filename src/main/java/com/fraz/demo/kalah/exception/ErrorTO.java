package com.fraz.demo.kalah.exception;

import org.springframework.http.HttpStatus;

import java.util.List;

public record ErrorTO(HttpStatus status, String message, List<String> errors) {}
