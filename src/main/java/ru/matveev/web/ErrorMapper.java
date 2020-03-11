package ru.matveev.web;

import ru.matveev.exceptions.LocalApplicationException;

import javax.json.Json;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ErrorMapper implements ExceptionMapper<LocalApplicationException> {

    @Override
    public Response toResponse(LocalApplicationException exception) {

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(Json.createObjectBuilder().add("error", exception.getMessage()).build())
                .build();
    }
}
