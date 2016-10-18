package com.acabra.calculator.resources;

import com.acabra.calculator.WebCalculatorManager;
import com.acabra.calculator.request.IntegralRequestDTO;
import com.acabra.calculator.response.*;
import com.acabra.calculator.util.RequestMapper;
import com.codahale.metrics.annotation.Timed;
import org.apache.log4j.Logger;
import org.glassfish.jersey.server.ManagedAsync;

import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URLDecoder;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author acabra
 * @created 2016-09-27
 */
@Path("/calculator")
@Produces(MediaType.APPLICATION_JSON)
public class WebCalculatorResource implements AppResource{


    private static final Logger logger = Logger.getLogger(WebCalculatorResource.class);
    /**
     *
     */
    private final AtomicLong counter;
    private static final String UTF8_ENC = "UTF-8";

    /**
     *
     */
    private final WebCalculatorManager webCalculatorManager;

    public WebCalculatorResource(WebCalculatorManager webCalculatorManager) {
        this.counter = new AtomicLong();
        this.webCalculatorManager = webCalculatorManager;
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
     * @param token
     */
    @GET
    @Timed
    @ManagedAsync
    @Path("/renderedhistory")
    public void retrieveRenderedHistoryResults(@Suspended final AsyncResponse asyncResponse, @QueryParam("token") String token) {
        CompletableFuture.supplyAsync(() -> {
            try {
                return getResponse(Response.Status.OK, "retrieved history", webCalculatorManager.provideRenderedHistoryResponse(token));
            }  catch (NoSuchElementException e) {
                logger.error(e);
                return getResponse(Response.Status.NOT_FOUND, "retrieving history: " + e.getMessage(), null);
            } catch (Exception e) {
                logger.error(e);
                return getResponse(Response.Status.INTERNAL_SERVER_ERROR, "retrieving history: " + e.getMessage(), null);
            }
        }).thenApply(asyncResponse::resume);
    }

    /**
     *
     * @param asyncResponse
     * @param token
     */
    @GET
    @Timed
    @ManagedAsync
    @Path("/history")
    public void retrieveRawHistoryResults(@Suspended final AsyncResponse asyncResponse, @QueryParam("token") String token) {
        CompletableFuture.supplyAsync(() -> {
            try {
                return getResponse(Response.Status.OK, "retrieved history", webCalculatorManager.provideHistoryResponse(token));
            }  catch (NoSuchElementException e) {
                logger.error(e);
                return getResponse(Response.Status.NOT_FOUND, "retrieving history: " + e.getMessage(), null);
            } catch (Exception e) {
                logger.error(e);
                return getResponse(Response.Status.INTERNAL_SERVER_ERROR, "retrieving history: " + e.getMessage(), null);
            }
        }).thenApply(asyncResponse::resume);
    }

    /**
     *
     * @param asyncResponse
     * @param token
     * @param integralRequestDTO
     */
    @POST
    @Timed
    @ManagedAsync
    @Path("/integral")
    @Consumes(MediaType.APPLICATION_JSON)
    public void resolveIntegral(@Suspended final AsyncResponse asyncResponse, @QueryParam("token") String token,
                                IntegralRequestDTO integralRequestDTO) {
        CompletableFuture.supplyAsync(() -> {
            try {
                CompletableFuture<CalculationResponse> integralFuture = webCalculatorManager.processIntegralCalculation(RequestMapper.fromInternalRequest(integralRequestDTO), token);
                return integralFuture.thenApply(integralResponse -> {
                    if (integralResponse.isFailure()) {
                        return getResponse(Response.Status.INTERNAL_SERVER_ERROR, integralResponse.getDescription(), null);
                    }
                    return getResponse(Response.Status.OK, "calculation performed", integralResponse);
                });
            } catch (Exception e) {
                logger.error(e);
                return CompletableFuture.completedFuture(getResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage(), null));
            }
        }).thenApply(responseFuture -> responseFuture.thenApply(asyncResponse::resume));
    }

    @POST
    @Timed
    @ManagedAsync
    public void makeCalculation(@Suspended final AsyncResponse asyncResponse, @QueryParam("expression") String expression,
                                @QueryParam("token") String token) {
        CompletableFuture.supplyAsync(() -> {
            try {
                logger.debug("encoded expression '" + expression + "'");
                String decodedExpression = URLDecoder.decode(expression, UTF8_ENC);
                logger.debug("decoded expression '" + decodedExpression + "'");
                return getResponse(Response.Status.OK, "calculation performed", webCalculatorManager.processArithmeticCalculation(decodedExpression, token));
            } catch (Exception e) {
                logger.error(e);
                return getResponse(Response.Status.INTERNAL_SERVER_ERROR, "calculating approximation: " + e.getMessage(), null);
            }
        }).thenApply(asyncResponse::resume);
    }

    @POST
    @Timed
    @ManagedAsync
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/token")
    public void provideSessionToken(@Suspended final AsyncResponse asyncResponse) {
        CompletableFuture.supplyAsync(() -> {
            try {
                String successMessage = "token retrieved successfully";
                return getResponse(Response.Status.OK, successMessage, webCalculatorManager.provideSessionToken());
            } catch (Exception e) {
                logger.error(e);
                return getResponse(Response.Status.INTERNAL_SERVER_ERROR, "calculating approximation: " + e.getMessage(), null);
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