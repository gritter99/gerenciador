package com.gerenciador.dto;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;

public record LoginRequest(@Email String email, @Length(min = 8, max = 255) String password) {}
