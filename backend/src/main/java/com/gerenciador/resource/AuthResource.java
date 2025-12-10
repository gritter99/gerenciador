package com.gerenciador.resource;

import java.util.UUID;
import org.eclipse.microprofile.jwt.Claim;
import com.gerenciador.dto.LoginRequest;
import com.gerenciador.dto.LoginResponse;
import com.gerenciador.dto.RegisterRequest;
import com.gerenciador.service.AuthService;
import com.gerenciador.service.UsuarioService;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@RequestScoped
@Path("/auth")
public class AuthResource {
    @Inject
    UsuarioService usuarioService;

    @Inject
    AuthService authService;

    @Inject
    @Claim("uuid")
    private String idUsuarioFromToken;
    
    @GET
    @PermitAll
    //@Produces(MediaType.APPLICATION_JSON)
    //@Consumes(MediaType.APPLICATION_JSON)
    public String hello() {
        return "Hello World";
    }

    @GET
    @Path("/user")
    @RolesAllowed("user")
    public Response helloUser() {
        var usuario = usuarioService.getUsuarioById(UUID.fromString(idUsuarioFromToken));
        return Response.ok(usuario).build();
    }

    @POST
    @Path("/register")
    @PermitAll
    public Response register(@Valid RegisterRequest registerRequest) {
        usuarioService.saveUsuario(registerRequest);
        return Response.status(201).build();
    }

    @POST
    @Path("/login")
    @PermitAll
    public LoginResponse login(@Valid LoginRequest loginRequest) {
        return authService.generateToken(loginRequest);
    }
}
