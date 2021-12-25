package com.acabra.mmind;

import com.acabra.calculator.resources.AppResource;
import com.acabra.calculator.response.SimpleResponse;
import com.acabra.mmind.request.MMindJoinRoomRequestDTO;
import com.acabra.mmind.request.MMindRequestDTO;
import com.acabra.mmind.response.MMindAuthResponse;
import com.acabra.mmind.response.MMindJoinRoomResponse;
import com.codahale.metrics.annotation.Timed;
import org.apache.log4j.Logger;
import org.glassfish.jersey.server.ManagedAsync;

import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;

@Path("/mmind")
@Produces(MediaType.APPLICATION_JSON)
public class MMindResource implements AppResource, AutoCloseable {

    private static final Logger logger = Logger.getLogger(MMindResource.class);
    private final MMindRoomManager roomManager = MMindRoomManager.of();
    private final AtomicLong idGen = new AtomicLong();

    @Override
    public Response getResponse(Response.Status status, String message, SimpleResponse body) {
        return Response.status(status).entity(body).build();
    }

    @Override
    public void close() throws Exception {

    }

    @POST
    @Timed
    @ManagedAsync
    @Path("/submit")
    @Consumes(MediaType.APPLICATION_JSON)
    public void guessNumber(@Suspended final AsyncResponse asyncResponse, MMindRequestDTO mMindRequestDTO) {
        CompletableFuture.supplyAsync(() -> {
            try {
                MMindManager mMindManager = roomManager.findRoom(mMindRequestDTO).getManager();
                return getResponse(Response.Status.OK, "guess submitted",
                        mMindManager.executeMove(this.idGen.incrementAndGet(), mMindRequestDTO));
            } catch (Exception e) {
                logger.error(e);
                return getResponse(Response.Status.INTERNAL_SERVER_ERROR, "submitted guess: " + e.getMessage(), null);
            }
        }).thenApply(asyncResponse::resume);
    }

    @POST
    @Timed
    @ManagedAsync
    @Path("/auth")
    @Consumes(MediaType.APPLICATION_JSON)
    public void authenticate(@Suspended final AsyncResponse asyncResponse, MMindJoinRoomRequestDTO request) {
        CompletableFuture.supplyAsync(() -> {
            try {
                MMindAuthResponse authResponse = roomManager.authenticate(request);
                return getResponse(Response.Status.OK, "guess submitted",
                        MMindJoinRoomResponse.builder()
                                .withId(idGen.incrementAndGet())
                                .withFailure(false)
                                .withToken(authResponse.getToken())
                                .withAction(authResponse.getAction().toString())
                                .withRoomPassword(authResponse.getRoomPassword())
                                .withRoomNumber(authResponse.getRoomNumber())
                                .withUserName(request.getPlayerName())
                                .build());
            } catch (Exception e) {
                logger.error(e);
                e.printStackTrace();
                return getResponse(Response.Status.INTERNAL_SERVER_ERROR, "submitted guess: " + e.getMessage(), null);
            }
        }).thenApply(asyncResponse::resume);
    }

    @GET
    @Timed
    @ManagedAsync
    @Path("/status")
    public void getConfig(@Suspended final AsyncResponse asyncResponse,
                          @QueryParam("token") String token, @QueryParam("room") long roomNumber) {
        CompletableFuture.supplyAsync(() -> {
            try {
                return getResponse(Response.Status.OK, "room status", roomManager.getStatus(idGen.incrementAndGet(),
                        token, roomNumber));
            } catch (Exception e) {
                logger.error(e);
                return getResponse(Response.Status.INTERNAL_SERVER_ERROR, "session limit reached please try again later: " + e.getMessage(), null);
            }
        }).thenApply(asyncResponse::resume);
    }
}
