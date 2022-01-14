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
            long id = idGen.incrementAndGet();
            try {
                FixSpikeRequestValidator.validateSecret(request.getGuess());
                FixSpikeGameManager manager = roomsAdmin.findRoomManager(request);
                if (manager.lastMoveExit()) {
                    throw new UnsupportedOperationException("Restart required: opponent left");
                }
                if(manager.hasTurn(request.getToken())) {
                    return getResponse(Response.Status.OK, "guess submitted", manager.attemptMove(id, request));
                }
                throw new UnsupportedOperationException("Not your turn");
            } catch (Exception e) {
                logger.error("error", e);
                return getResponse(Response.Status.INTERNAL_SERVER_ERROR, "submitted guess: " + e.getMessage(),
                        error(id, "unable to accept number", e));
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
            long id = idGen.incrementAndGet();
            try {
                FixSpikeRequestValidator.validateJoinRequest(request);
                return getResponse(Response.Status.OK, "guess submitted",
                        roomsAdmin.attemptAuthenticate(id, request));
            } catch (Exception e) {
                logger.error("error", e);
                e.printStackTrace();
                return getResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage(),
                        error(id, "unable to authenticate", e));
            }
        }).thenApply(asyncResponse::resume);
    }

    @GET
    @Timed
    @ManagedAsync
    @Path("/status")
    public void getConfig(@Suspended final AsyncResponse asyncResponse,
                          @QueryParam("token") String token, @QueryParam("room") long roomNumber,
                          @QueryParam("seq") Long seq) {
        CompletableFuture.supplyAsync(() -> {
            long id = idGen.incrementAndGet();
            try {
                return getResponse(Response.Status.OK, "room status", roomsAdmin.getStatus(id, seq, token, roomNumber));
            } catch (Exception e) {
                logger.error("error", e);
                return getResponse(Response.Status.INTERNAL_SERVER_ERROR,
                        "session limit reached please try again later: " + e.getMessage(),
                        error(id, "unable to retrieve status", e));
            }
        }).thenApply(asyncResponse::resume);
    }

    @GET
    @Timed
    @ManagedAsync
    @Path("/admin")
    public void viewSystemStats(@Suspended final AsyncResponse asyncResponse, @NonNull @QueryParam("token") String token) {
        CompletableFuture.supplyAsync(() -> {
            long id = idGen.incrementAndGet();
            try {
                return getResponse(Response.Status.OK, "room status",
                        roomsAdmin.reviewSystemStatus(id, token));
            } catch (Exception e) {
                logger.error("error", e);
                return getResponse(Response.Status.INTERNAL_SERVER_ERROR,
                        "session limit reached please try again later: " + e.getMessage(),
                        error(id, "unable to retrieve statistics for admin", e));
            }
        }).thenApply(asyncResponse::resume);
    }

    @GET
    @Timed
    @ManagedAsync
    @Path("/exit")
    public void exitRoom(@Suspended final AsyncResponse asyncResponse, @NonNull @QueryParam("token") String token,
                         @QueryParam("room") long roomNumber) {
        CompletableFuture.supplyAsync(() -> {
            long id = idGen.incrementAndGet();
            try {
                CompletableFuture.supplyAsync(() -> roomsAdmin.exitRoom(id, token, roomNumber));
                return getResponse(Response.Status.OK, "room status", roomsAdmin.okResponse(id));
            } catch (Exception e) {
                logger.error("error", e);
                return getResponse(Response.Status.INTERNAL_SERVER_ERROR,
                        "unable to register player's exit: " + e.getMessage(),
                        error(id, "unable to register player's exit", e));
            }
        }).thenApply(asyncResponse::resume);
    }

    @POST
    @Timed
    @ManagedAsync
    @Path("/restart")
    public void viewSystemStats(@Suspended final AsyncResponse asyncResponse, FixSpikeRestartRequest req) {
        CompletableFuture.supplyAsync(() -> {
            long id = idGen.incrementAndGet();
            try {
                FixSpikeRequestValidator.validateSecret(req.getSecret());
                return getResponse(Response.Status.OK, "room status",
                        roomsAdmin.processRestartRequest(id, req));
            } catch (Exception e) {
                logger.error("error", e);
                return getResponse(Response.Status.INTERNAL_SERVER_ERROR,
                        "Unable To restart: " + e.getMessage(),
                        error(id, "unable to process restart", e));
            }
        }).thenApply(asyncResponse::resume);
    }

    @DELETE
    @Timed
    @ManagedAsync
    @Path("/token")
    public void deleteToken(@Suspended final AsyncResponse asyncResponse, FixSpikeDeleteTokenRequest req) {
        CompletableFuture.supplyAsync(() -> {
            long id = idGen.incrementAndGet();
            try {
                FixSpikeRequestValidator.validateDeleteTokenRequest(req);
                final SimpleResponse deleteResult = roomsAdmin.processDeleteTokenRequest(id, req);
                return getResponse(deleteResult.isFailure() ? Response.Status.BAD_REQUEST : Response.Status.OK,
                        "delete status", deleteResult);
            } catch (Exception e) {
                logger.error("error", e);
                return getResponse(Response.Status.INTERNAL_SERVER_ERROR,
                        "Unable To process delete request: " + e.getMessage(),
                        error(id, "unable to process delete", e));
            }
        }).thenApply(asyncResponse::resume);
    }

    @DELETE
    @Timed
    @ManagedAsync
    @Path("/room")
    public void deleteRoom(@Suspended final AsyncResponse asyncResponse, FixSpikeDeleteRoomRequest req) {
        CompletableFuture.supplyAsync(() -> {
            long id = idGen.incrementAndGet();
            try {
                FixSpikeRequestValidator.validateDeleteRoomRequest(req);
                final FixSpikeDeleteRoomResponse deleteResult = roomsAdmin.processDeleteRoomRequest(id, req);
                return getResponse(deleteResult.isFailure() ? Response.Status.NOT_FOUND : Response.Status.OK,
                        deleteResult.getMessage(), deleteResult);
            } catch (Exception e) {
                logger.error("error", e);
                return getResponse(Response.Status.INTERNAL_SERVER_ERROR,
                        "Unable To process delete request: " + e.getMessage(),
                        error(id, "unable to process delete", e));
            }
        }).thenApply(asyncResponse::resume);
    }
}
