package com.acabra.calculator.resources;

import com.acabra.calculator.WebCalculatorManager;
import com.acabra.calculator.request.IntegralRequestDTO;
import com.acabra.calculator.response.MessageResponse;
import com.acabra.calculator.response.SimpleResponse;
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author acabra
 * @created 2016-09-27
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class WebCalculatorResource implements AppResource {


    private static final Logger logger = Logger.getLogger(WebCalculatorResource.class);
    private final AtomicLong counter;
    private static final String UTF8_ENC = "UTF-8";
    private final WebCalculatorManager webCalculatorManager;

    public WebCalculatorResource(WebCalculatorManager webCalculatorManager) {
        this.counter = new AtomicLong();
        this.webCalculatorManager = webCalculatorManager;
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
                return getResponse(Response.Status.OK, "retrieved history", webCalculatorManager.provideRenderedHistoryResult(token));
            }  catch (Exception e) {
                return getResponse(Response.Status.INTERNAL_SERVER_ERROR, "retrieving history: " + e.getMessage(), null);
            }
        }).thenApply(asyncResponse::resume);
    }

    @POST
    @Timed
    @ManagedAsync
    @Path("/integral")
    @Consumes(MediaType.APPLICATION_JSON)
    public void resolveIntegral(@Suspended final AsyncResponse asyncResponse, @QueryParam("token") String token,
                                IntegralRequestDTO integralRequestDTO) {
        CompletableFuture.supplyAsync(() -> {
            try {
                return webCalculatorManager.processExponentialIntegralCalculation(RequestMapper.fromInternalRequest(integralRequestDTO), token)
                        .thenApply(calculationResponse -> getResponse(Response.Status.OK, "calculation performed", calculationResponse))
                        .get();
            } catch (Exception e) {
                return getResponse(Response.Status.INTERNAL_SERVER_ERROR, "calculating result: " + e.getMessage(), null);
            }
        }).thenApply(asyncResponse::resume);
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
                return getResponse(Response.Status.INTERNAL_SERVER_ERROR, "calculating result: " + e.getMessage(), null);
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
                return getResponse(Response.Status.INTERNAL_SERVER_ERROR, "calculating result: " + e.getMessage(), null);
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