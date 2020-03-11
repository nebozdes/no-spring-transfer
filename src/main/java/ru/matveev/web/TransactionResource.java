package ru.matveev.web;

import ru.matveev.model.Transaction;
import ru.matveev.service.Transactions;
import ru.matveev.web.dto.TransactionRequest;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.Optional;

import static ru.matveev.Constants.DEFAULT_LIMIT;
import static ru.matveev.Constants.DEFAULT_PAGE;

@Path("/transaction")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TransactionResource {

    private final Transactions transactions;

    @Inject
    public TransactionResource(Transactions transactions) {
        this.transactions = transactions;
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    public Response transfer(@Valid TransactionRequest request) {

        Transaction transaction = transactions.create(request.getFromAccountId(), request.getToAccountId(), request.getAmount());

        return Response.ok(transaction).build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response list(
            @QueryParam("page") Integer page,
            @QueryParam("limit") Integer limit
    ) {
        return Response.ok(
                transactions.list(
                        Optional.ofNullable(page).orElse(DEFAULT_PAGE),
                        Optional.ofNullable(limit).orElse(DEFAULT_LIMIT)
                )
        ).build();
    }
}