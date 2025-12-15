package com.gerenciador.resource;

import java.util.UUID;

import org.eclipse.microprofile.jwt.Claim;

import com.gerenciador.dto.DashboardResponse;
import com.gerenciador.service.DashboardService;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@RequestScoped
@Path("/dashboard")
@Produces(MediaType.APPLICATION_JSON)
public class DashboardResource {

    @Inject
    DashboardService dashboardService;

    @Inject
    @Claim("uuid")
    String usuarioIdFromToken;

    @GET
    @RolesAllowed("user")
    public Response getResumo() {
        UUID usuarioId = UUID.fromString(usuarioIdFromToken);
        DashboardResponse dashboardResponse = dashboardService.calcularResumo(usuarioId);
        return Response.ok(dashboardResponse).build();
    }
}
