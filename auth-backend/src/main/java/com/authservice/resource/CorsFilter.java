package com.authservice.resource;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;

@Provider
public class CorsFilter implements ContainerResponseFilter {

    private static final String CORS_ALLOWED_ORIGIN =
            System.getenv("CORS_ALLOWED_ORIGIN") != null
                    ? System.getenv("CORS_ALLOWED_ORIGIN")
                    : "http://localhost:4200";

    @Override
    public void filter(ContainerRequestContext requestContext,
                       ContainerResponseContext responseContext) {

        for (String origin : CORS_ALLOWED_ORIGIN.split(",")) {
            String requestOrigin = requestContext.getHeaderString("Origin");
            if (origin.trim().equals(requestOrigin)) {
                responseContext.getHeaders().add("Access-Control-Allow-Origin", requestOrigin);
                break;
            }
        }

        responseContext.getHeaders().add("Access-Control-Allow-Credentials", "true");
        responseContext.getHeaders().add("Access-Control-Allow-Headers",
                "origin, content-type, accept, authorization");
        responseContext.getHeaders().add("Access-Control-Allow-Methods",
                "GET, POST, PUT, DELETE, OPTIONS, HEAD");
    }
}