package com.acabra.resources;

import com.acabra.domain.response.MessageResponse;
import com.acabra.domain.response.SimpleResponse;
import com.codahale.metrics.annotation.Timed;
import org.glassfish.jersey.server.ManagedAsync;

import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author acabra
 * @created 2016-09-27
 */
@Path("/webcalculator")
@Produces(MediaType.APPLICATION_JSON)
public class WebCalculatorResource implements AppResource {

    private final AtomicLong counter;

    public WebCalculatorResource() {
        this.counter = new AtomicLong();
    }

    @Override
    public Response getResponse(Response.Status status, String message, SimpleResponse response) {
        return Response.status(status)
                .entity(new MessageResponse(this.counter.incrementAndGet(), message, response))
                .build();
    }

    @GET
    @Timed
    @ManagedAsync
    @Path("/history")
    @Consumes(MediaType.APPLICATION_JSON)
    public void retrieveHistoryResults(@Suspended final AsyncResponse asyncResponse, @QueryParam("token") String token) {
        CompletableFuture.supplyAsync(() -> {
            try {
                return getResponse(Response.Status.NOT_FOUND, "No authentication contexts found", null);
            }  catch (Exception e) {
                return getResponse(Response.Status.INTERNAL_SERVER_ERROR, "Error calculating Geolocation Indicators: " + e.getMessage(), null);
            }
        }).thenApply(asyncResponse::resume);
    }

    @POST
    @Timed
    @ManagedAsync
    @Consumes(MediaType.APPLICATION_JSON)
    public void makeCalculation(@Suspended final AsyncResponse asyncResponse, String expression) {
        CompletableFuture.supplyAsync(() -> {
            try {

                return getResponse(Response.Status.NOT_FOUND, "No information was retrieved from the database", null);
            } catch (Exception e) {
                return getResponse(Response.Status.INTERNAL_SERVER_ERROR, "Error calculating risk context for current authentication: " + e.getMessage(), null);
            }
        }).thenApply(asyncResponse::resume);
    }
}

/**
 * Example:
 *
 @POST
 @Path("{appNum}/{docId}/file")
 public void test(
 @PathParam("appNum") String appNum,
 @PathParam("docId") String docId,
 @QueryParam("single-token") Optional<String> token,
 @Context HttpServletRequest req,
 @Context HttpHeaders headers)
 **/