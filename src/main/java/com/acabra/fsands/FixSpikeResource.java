package com.acabra.fsands;

import com.acabra.calculator.resources.AppResource;
import com.acabra.calculator.response.SimpleResponse;
import com.acabra.fsands.auth.FixSpikeRequestValidator;
import com.acabra.fsands.request.*;
import com.acabra.fsands.response.ErrorResponse;
import com.acabra.fsands.response.FixSpikeDeleteRoomResponse;
import com.acabra.shared.CommonExecutorService;
import com.codahale.metrics.annotation.Timed;
import lombok.NonNull;

import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.server.ManagedAsync;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Path("/fsands")
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class FixSpikeResource implements AppResource {

    private final FixSpikeRoomsAdministrator roomsAdmin = FixSpikeRoomsAdministrator.of();
    private final AtomicLong idGen = new AtomicLong();

    @Inject
    public FixSpikeResource(CommonExecutorService executorService) {
        final int thirtyMinutesAsSeconds = (int) TimeUnit.MINUTES.toSeconds(30);
        executorService.scheduleAtFixedRate(() -> {
            logger.info("automatic room cleanup");
            roomsAdmin.clean();
        }, 13, thirtyMinutesAsSeconds, TimeUnit.SECONDS);
    }

    private static SimpleResponse error(long id, String msg, Exception exception) {
        return ErrorResponse.builder()
                .withId(id)
                .withError(String.format("ExceptionType:[%s:%s] " + msg, exception.getClass().getName(), exception.getMessage()))
                .build();
    }

    @Override
    public Response getResponse(Response.Status status, String message, SimpleResponse body) {
        return Response.status(status).entity(body).build();
    }

    @POST
    @Timed
    @ManagedAsync
    @Path("/submit")
    @Consumes(MediaType.APPLICATION_JSON)
    public void guessNumber(@Suspended final AsyncResponse asyncResponse, FixSpikeRequestDTO request) {
        CompletableFuture.supplyAsync(() -> {
            try {
                FixSpikeRequestValidator.validateSecret(request.getGuess());
                FixSpikeGameManager manager = roomsAdmin.findRoomManager(request);
                if(manager.hasMove(request.getToken())) {
                    return getResponse(Response.Status.OK, "guess submitted",
                            manager.attemptMove(this.idGen.incrementAndGet(), request));
                }
                throw new UnsupportedOperationException("Not your turn");
            } catch (Exception e) {
                logger.error("error", e);
                return getResponse(Response.Status.INTERNAL_SERVER_ERROR, "submitted guess: " + e.getMessage(),
                        error(idGen.incrementAndGet(), "unable to accept number", e));
            }
        }).thenApply(asyncResponse::resume);
    }

    @POST
    @Timed
    @ManagedAsync
    @Path("/auth")
    @Consumes(MediaType.APPLICATION_JSON)
    public void authenticate(@Suspended final AsyncResponse asyncResponse, FixSpikeJoinRoomRequestDTO request) {
        CompletableFuture.supplyAsync(() -> {
            try {
                FixSpikeRequestValidator.validateJoinRequest(request);
                return getResponse(Response.Status.OK, "guess submitted",
                        roomsAdmin.attemptAuthenticate(idGen.incrementAndGet(), request));
            } catch (Exception e) {
                logger.error("error", e);
                e.printStackTrace();
                return getResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage(),
                        error(idGen.incrementAndGet(), "unable to authenticate", e));
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
                return getResponse(Response.Status.OK, "room status", roomsAdmin.getStatus(idGen.incrementAndGet(),
                        token, roomNumber));
            } catch (Exception e) {
                logger.error("error", e);
                return getResponse(Response.Status.INTERNAL_SERVER_ERROR,
                        "session limit reached please try again later: " + e.getMessage(),
                        error(idGen.incrementAndGet(), "unable to retrieve status", e));
            }
        }).thenApply(asyncResponse::resume);
    }

    @GET
    @Timed
    @ManagedAsync
    @Path("/admin")
    public void viewSystemStats(@Suspended final AsyncResponse asyncResponse, @NonNull @QueryParam("token") String token) {
        CompletableFuture.supplyAsync(() -> {
            try {
                return getResponse(Response.Status.OK, "room status",
                        roomsAdmin.reviewSystemStatus(idGen.incrementAndGet(), token));
            } catch (Exception e) {
                logger.error("error", e);
                return getResponse(Response.Status.INTERNAL_SERVER_ERROR,
                        "session limit reached please try again later: " + e.getMessage(),
                        error(idGen.incrementAndGet(), "unable to retrieve statistics for admin", e));
            }
        }).thenApply(asyncResponse::resume);
    }

    @POST
    @Timed
    @ManagedAsync
    @Path("/restart")
    public void viewSystemStats(@Suspended final AsyncResponse asyncResponse, FixSpikeRestartRequest req) {
        CompletableFuture.supplyAsync(() -> {
            try {
                FixSpikeRequestValidator.validateSecret(req.getSecret());
                return getResponse(Response.Status.OK, "room status",
                        roomsAdmin.processRestartRequest(idGen.incrementAndGet(), req));
            } catch (Exception e) {
                logger.error("error", e);
                return getResponse(Response.Status.INTERNAL_SERVER_ERROR,
                        "Unable To restart: " + e.getMessage(),
                        error(idGen.incrementAndGet(), "unable to process restart", e));
            }
        }).thenApply(asyncResponse::resume);
    }

    @DELETE
    @Timed
    @ManagedAsync
    @Path("/token")
    public void deleteToken(@Suspended final AsyncResponse asyncResponse, FixSpikeDeleteTokenRequest req) {
        CompletableFuture.supplyAsync(() -> {
            try {
                FixSpikeRequestValidator.validateDeleteTokenRequest(req);
                final SimpleResponse deleteResult = roomsAdmin.processDeleteTokenRequest(idGen.incrementAndGet(), req);
                return getResponse(deleteResult.isFailure() ? Response.Status.BAD_REQUEST : Response.Status.OK,
                        "delete status", deleteResult);
            } catch (Exception e) {
                logger.error("error", e);
                return getResponse(Response.Status.INTERNAL_SERVER_ERROR,
                        "Unable To process delete request: " + e.getMessage(),
                        error(idGen.incrementAndGet(), "unable to process delete", e));
            }
        }).thenApply(asyncResponse::resume);
    }

    @DELETE
    @Timed
    @ManagedAsync
    @Path("/room")
    public void deleteRoom(@Suspended final AsyncResponse asyncResponse, FixSpikeDeleteRoomRequest req) {
        CompletableFuture.supplyAsync(() -> {
            try {
                FixSpikeRequestValidator.validateDeleteRoomRequest(req);
                final FixSpikeDeleteRoomResponse deleteResult = roomsAdmin.processDeleteRoomRequest(idGen.incrementAndGet(), req);
                return getResponse(deleteResult.isFailure() ? Response.Status.NOT_FOUND : Response.Status.OK,
                        deleteResult.getMessage(), deleteResult);
            } catch (Exception e) {
                logger.error("error", e);
                return getResponse(Response.Status.INTERNAL_SERVER_ERROR,
                        "Unable To process delete request: " + e.getMessage(),
                        error(idGen.incrementAndGet(), "unable to process delete", e));
            }
        }).thenApply(asyncResponse::resume);
    }
}
