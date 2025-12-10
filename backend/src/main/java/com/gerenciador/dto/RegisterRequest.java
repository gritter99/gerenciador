package com.gerenciador.dto;

import org.hibernate.validator.constraints.Length;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


public record RegisterRequest (@Length(min = 3, max = 255) @NotBlank String nome, @Email String email, @Length(min = 8, max = 255) String senha) {}
