package com.acabra.roulette.resource;

import com.acabra.calculator.resources.AppResource;
import com.acabra.calculator.response.MessageResponse;
import com.acabra.calculator.response.SimpleResponse;
import com.acabra.roulette.RouletteManager;
import com.acabra.roulette.request.RouletteRequestDTO;
import com.codahale.metrics.annotation.Timed;
import org.apache.log4j.Logger;
import org.glassfish.jersey.server.ManagedAsync;

import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

@Path("/roulette")
@Produces(MediaType.APPLICATION_JSON)
public class RouletteResource implements AppResource, AutoCloseable {

    private static final Logger logger = Logger.getLogger(RouletteResource.class);

    /**
     *
     */
    private static final String UTF8_ENC = "UTF-8";
    private static final int MAX_CONCURRENT_SESSIONS = 50;
    private static final long EXPIRE_TIMEOUT = 1800000L; // 30 minutes
    private AtomicLong counter = new AtomicLong();
    private SecureRandom secureRandom = new SecureRandom();
    private Map<Long, RouletteManager> rouletteManagerMap = new ConcurrentHashMap<>();
    private ScheduledExecutorService ex;
    private final Executor spinDispatcher = Executors.newSingleThreadScheduledExecutor();

    public RouletteResource() {
        this.ex = Executors.newSingleThreadScheduledExecutor();
        ex.scheduleAtFixedRate(() -> {
            logger.info("automatic cleanup of entries");
            cleanUpExpiredSessions(System.currentTimeMillis(), this.rouletteManagerMap);
        }, 30, 30, TimeUnit.SECONDS);
    }

    synchronized private RouletteManager buildNewManager() {
        if (rouletteManagerMap.size() >= MAX_CONCURRENT_SESSIONS) {
            logger.info("try to free space trigger manual cleanup of entries");
            cleanUpExpiredSessions(System.currentTimeMillis(), rouletteManagerMap);
            if (rouletteManagerMap.size() >= MAX_CONCURRENT_SESSIONS) {
                logger.error("not available sessions to drop");
                return null;
            }
        }
        RouletteManager newRouletteManager = new RouletteManager(this.secureRandom.nextLong(), spinDispatcher);
        logger.info("new manager created with id:" + newRouletteManager.getId());
        this.rouletteManagerMap.put(newRouletteManager.getId(), newRouletteManager);
        return newRouletteManager;
    }

    private static void cleanUpExpiredSessions(long currentTime, Map<Long, RouletteManager> rouletteManagerMap) {
        if(rouletteManagerMap.size() > 1) {
            List<Long> expiredSessions = new ArrayList<>();
            rouletteManagerMap.values().stream()
                    .filter(manager -> currentTime - manager.getLastAccessed() > EXPIRE_TIMEOUT)
                    .sorted((a,b) -> Long.compare(b.getLastAccessed(), a.getLastAccessed()))
                    .mapToLong(RouletteManager::getId)
                    .forEach(expiredSessions::add);
            //remove all expired sessions except one
            expiredSessions.forEach(sessionId -> {
                rouletteManagerMap.remove(sessionId);
                logger.info("cleaned manager:" + sessionId);
            });
        }
    }

    @Override
    public Response getResponse(Response.Status status, String message, SimpleResponse object) {
        return Response.status(status)
                .entity(new MessageResponse<>(counter.getAndIncrement(), !status.equals(Response.Status.OK), message, object))
                .build();
    }

    /**
     *
     * @param asyncResponse
     */
    @GET
    @Timed
    @ManagedAsync
    @Path("/config")
    public void getConfig(@Suspended final AsyncResponse asyncResponse) {
        CompletableFuture.supplyAsync(() -> {
            try {
                RouletteManager rouletteManager = Objects.requireNonNull(buildNewManager());
                return getResponse(Response.Status.OK, "roulette config", rouletteManager.getConfig());
            }  catch (NoSuchElementException e) {
                logger.error(e);
                return getResponse(Response.Status.NOT_FOUND, "roulette config: " + e.getMessage(), null);
            } catch (Exception e) {
                logger.error(e);
                return getResponse(Response.Status.INTERNAL_SERVER_ERROR, "session limit reached please try again later: " + e.getMessage(), null);
            }
        }).thenApply(asyncResponse::resume);
    }

    /**
     *
     * @param asyncResponse
     * @param number
     */
    @POST
    @Timed
    @ManagedAsync
    @Path("/submit")
    @Consumes(MediaType.APPLICATION_JSON)
    public void manuallyAddNumber(@Suspended final AsyncResponse asyncResponse, @QueryParam("number") Integer number,
                                  RouletteRequestDTO rouletteRequestDTO) {
        CompletableFuture.supplyAsync(() -> {
            try {
                Long token = Long.parseLong(rouletteRequestDTO.getToken());
                RouletteManager rouletteManager = Objects.requireNonNull(
                        rouletteManagerMap.getOrDefault(token, null));
                return getResponse(Response.Status.OK, "number submitted", rouletteManager.addResult(number));
            }  catch (NoSuchElementException e) {
                logger.error(e);
                return getResponse(Response.Status.NOT_FOUND, "submitted number: " + e.getMessage(), null);
            } catch (Exception e) {
                logger.error(e);
                return getResponse(Response.Status.INTERNAL_SERVER_ERROR, "submitted number: " + e.getMessage(), null);
            }
        }).thenApply(asyncResponse::resume);
    }

    /**
     *
     * @param asyncResponse
     */
    @POST
    @Timed
    @ManagedAsync
    @Path("/spin")
    @Consumes(MediaType.APPLICATION_JSON)
    public void spinRoulette(@Suspended final AsyncResponse asyncResponse, RouletteRequestDTO rouletteRequestDTO) {
        CompletableFuture.supplyAsync(() -> {
            try {
                Long sessionToken = Long.parseLong(rouletteRequestDTO.getToken());
                logger.info(sessionToken);
                RouletteManager rouletteManager = Objects.requireNonNull(
                        rouletteManagerMap.getOrDefault(sessionToken, null));
                return getResponse(Response.Status.OK, "spin roulette", rouletteManager.spinRoulette());
            }  catch (NoSuchElementException e) {
                logger.error(e);
                return getResponse(Response.Status.NOT_FOUND, "spin roulette: " + e.getMessage(), null);
            } catch (Exception e) {
                logger.error(e);
                return getResponse(Response.Status.INTERNAL_SERVER_ERROR, "invalid session: " + e.getMessage(), null);
            }
        }).thenApply(asyncResponse::resume);
    }

    @Override
    public void close() throws Exception {
        if (!ex.awaitTermination(1, TimeUnit.SECONDS)) {
            ex.shutdownNow();
        }
    }
}
