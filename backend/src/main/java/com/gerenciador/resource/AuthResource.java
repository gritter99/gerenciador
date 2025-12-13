package com.gerenciador.resource;

import com.gerenciador.dto.LoginRequest;
import com.gerenciador.dto.LoginResponse;
import com.gerenciador.dto.RegisterRequest;
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
import jakarta.ws.rs.core.Response;

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
    public Response register(@Valid RegisterRequest request) {
        usuarioService.saveUsuario(request);
        return Response.status(Response.Status.CREATED).build();
    }

    @POST
    @Path("/login")
    @PermitAll
    public LoginResponse login(@Valid LoginRequest request) {
        return authService.generateToken(request);
    }
}
