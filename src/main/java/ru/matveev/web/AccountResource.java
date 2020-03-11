package ru.matveev.web;

import ru.matveev.service.Accounts;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.Optional;

import static ru.matveev.Constants.DEFAULT_LIMIT;
import static ru.matveev.Constants.DEFAULT_PAGE;

@Path("/account")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AccountResource {

    private final Accounts accounts;

    @Inject
    public AccountResource(Accounts accounts) {
        this.accounts = accounts;
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response get(@Valid @Positive @PathParam("id") Long accountId) {
        return Response.ok(accounts.get(accountId)).build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response list(
            @QueryParam("page") Integer page,
            @QueryParam("limit") Integer limit
    ) {
        return Response.ok(
                accounts.list(
                        Optional.ofNullable(page).orElse(DEFAULT_PAGE),
                        Optional.ofNullable(limit).orElse(DEFAULT_LIMIT)
                )
        ).build();
    }
}