package com.gerenciador.resource;

import com.gerenciador.dto.AuthResponse;
import com.gerenciador.dto.AuthResponse.UsuarioResponse;
import com.gerenciador.dto.LoginRequest;
import com.gerenciador.dto.RegisterRequest;
import com.gerenciador.entity.Usuario;
import com.gerenciador.repository.UsuarioRepository;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
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
    UsuarioRepository usuarioRepository;

    @POST
    @Path("/register")
    @Transactional
    public Response register(@Valid RegisterRequest request) {
        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            return Response.status(Response.Status.CONFLICT).entity("Email já cadastrado").build();
        }

        Usuario usuario = new Usuario();
        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());
        usuario.setSenhaHash(request.getSenha());
        usuarioRepository.persist(usuario);

        String tokenTemporario = "token-temporario-" + usuario.getId().toString();
        UsuarioResponse usuarioResponse = new UsuarioResponse(
                usuario.getId(), usuario.getNome(), usuario.getEmail());

        return Response.status(Response.Status.CREATED).entity(new AuthResponse(tokenTemporario, usuarioResponse))
                .build();
    }

    @POST
    @Path("/login")
    public Response login(@Valid LoginRequest request) {
        return usuarioRepository.findByEmail(request.getEmail())
                .map(usuario -> {
                    // (comparação simples por enquanto - será bcrypt depois)
                    if (!usuario.getSenhaHash().equals(request.getSenha())) {
                        return Response.status(Response.Status.UNAUTHORIZED)
                                .entity("{\"error\": \"Credenciais inválidas\"}")
                                .build();
                    }

                    // gerar resposta com token temporário
                    String tokenTemporario = "token-temporario-" + usuario.getId().toString();
                    UsuarioResponse usuarioResponse = new UsuarioResponse(
                            usuario.getId(),
                            usuario.getNome(),
                            usuario.getEmail());

                    return Response.ok(new AuthResponse(tokenTemporario, usuarioResponse)).build();
                })
                .orElse(Response.status(Response.Status.UNAUTHORIZED)
                        .entity("{\"error\": \"Credenciais inválidas\"}")
                        .build());
    }
}