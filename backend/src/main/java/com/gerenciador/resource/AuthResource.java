package com.gerenciador.resource;

import com.gerenciador.dto.AuthResponse;
import com.gerenciador.dto.LoginRequest;
import com.gerenciador.dto.RegisterRequest;
import com.gerenciador.security.jwt.GenerateToken;
import com.gerenciador.service.AuthService;
import com.gerenciador.service.UsuarioService;

import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    UsuarioService usuarioService;

    @Inject
    AuthService authService;

    @POST
    @Path("/register")
    @PermitAll
    public AuthResponse register(@Valid RegisterRequest request) {
        var usuario = usuarioService.saveUsuario(request);
        var token = GenerateToken.generateToken(usuario.getEmail(), usuario.getId());
        var usuarioResponse = new AuthResponse.UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail());
        return new AuthResponse(token, usuarioResponse);
    }

    @POST
    @Path("/login")
    @PermitAll
    public AuthResponse login(@Valid LoginRequest request) {
        return authService.authenticate(request);
    }
}
