package com.authservice.resource;

import com.authservice.dto.AuthDTO;
import com.authservice.entity.User;
import com.authservice.service.AuthService;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    AuthService authService;

    @Inject
    JsonWebToken jwt;

    @POST
    @Path("/register")
    @Authenticated
    public Response register(AuthDTO.RegisterRequest request) {
        AuthDTO.AuthResponse response = authService.register(request);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @POST
    @Path("/login")
    public Response login(AuthDTO.LoginRequest request) {
        AuthDTO.AuthResponse response = authService.login(request);
        return Response.ok(response).build();
    }

    @GET
    @Path("/me")
    @Authenticated
    public Response me() {
        Long userId = Long.parseLong(jwt.getSubject());
        User user = User.findById(userId);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        AuthDTO.UserResponse response = new AuthDTO.UserResponse(
                user.id,
                user.name,
                user.email,
                user.createdAt.toString()
        );
        return Response.ok(response).build();
    }

    @PUT
    @Path("/me")
    @Authenticated
    public Response updateProfile(AuthDTO.UpdateProfileRequest request) {
        Long userId = Long.parseLong(jwt.getSubject());
        authService.updateProfile(userId, request);
        return Response.noContent().build();
    }

    @PUT
    @Path("/me/password")
    @Authenticated
    public Response updatePassword(AuthDTO.UpdatePasswordRequest request) {
        Long userId = Long.parseLong(jwt.getSubject());
        authService.updatePassword(userId, request);
        return Response.noContent().build();
    }
}