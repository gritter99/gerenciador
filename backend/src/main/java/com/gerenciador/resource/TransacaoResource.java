package com.gerenciador.resource;

import java.util.List;
import java.util.UUID;

import org.eclipse.microprofile.jwt.Claim;

import com.gerenciador.dto.TransacaoRequest;
import com.gerenciador.dto.TransacaoResponse;
import com.gerenciador.service.TransacaoService;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@RequestScoped
@Path("/transacoes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TransacaoResource {

    @Inject
    TransacaoService transacaoService;

    @Inject
    @Claim("uuid")
    String usuarioIdFromToken;

    @POST
    @RolesAllowed("user")
    public Response criar(@Valid TransacaoRequest request) {
        UUID usuarioId = UUID.fromString(usuarioIdFromToken);
        TransacaoResponse response = transacaoService.criar(request, usuarioId);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @GET
    @RolesAllowed("user")
    public List<TransacaoResponse> listar() {
        UUID usuarioId = UUID.fromString(usuarioIdFromToken);
        return transacaoService.listar(usuarioId);
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("user")
    public Response excluir(@PathParam("id") UUID id) {
        UUID usuarioId = UUID.fromString(usuarioIdFromToken);
        transacaoService.excluir(id, usuarioId);
        return Response.noContent().build();
    }
}
