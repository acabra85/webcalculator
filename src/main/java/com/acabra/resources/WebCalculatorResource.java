package com.acabra.resources;

import com.acabra.calculator.CalculatorManager;
import com.acabra.domain.response.CalculationResponse;
import com.acabra.domain.response.MessageResponse;
import com.acabra.domain.response.SimpleResponse;
import com.acabra.domain.response.TokenResponse;
import com.acabra.view.WebCalculatorRendererHTML;
import com.codahale.metrics.annotation.Timed;
import org.apache.log4j.Logger;
import org.glassfish.jersey.server.ManagedAsync;

import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author acabra
 * @created 2016-09-27
 */
@Path("/webcalculator")
@Produces(MediaType.APPLICATION_JSON)
public class WebCalculatorResource implements AppResource {


    private static final Logger logger = Logger.getLogger(WebCalculatorResource.class);
    private final AtomicLong counter;
    final static String INDEX_FILE_URI = "web/index.html";
    private static final String UTF8_ENC = "UTF-8";
    private final CalculatorManager calculatorManager;

    public WebCalculatorResource(CalculatorManager calculatorManager) {
        this.counter = new AtomicLong();
        this.calculatorManager = calculatorManager;
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
    @Produces(MediaType.TEXT_HTML)
    public InputStream provideHomeDirectory() {
        return WebCalculatorResource.class.getClassLoader().getResourceAsStream(INDEX_FILE_URI);
    }

    @GET
    @Timed
    @ManagedAsync
    @Path("/history")
    @Consumes(MediaType.APPLICATION_JSON)
    public void retrieveHistoryResults(@Suspended final AsyncResponse asyncResponse, @QueryParam("token") String token) {
        CompletableFuture.supplyAsync(() -> {
            try {
                return getResponse(Response.Status.OK, "retrieved history", calculatorManager.provideRenderedHistoryResult(token));
            }  catch (Exception e) {
                return getResponse(Response.Status.INTERNAL_SERVER_ERROR, "Error retrieving history: " + e.getMessage(), null);
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
                logger.info("encoded expression '" + expression + "'");
                String decodedExpression = URLDecoder.decode(expression, UTF8_ENC);
                logger.info("decoded expression '" + decodedExpression + "'");
                return getResponse(Response.Status.OK, "calculation performed", calculatorManager.processCalculation(decodedExpression, token));
            } catch (Exception e) {
                return getResponse(Response.Status.INTERNAL_SERVER_ERROR, "Error calculating result: " + e.getMessage(), null);
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
                return getResponse(Response.Status.OK, successMessage, new TokenResponse(counter.incrementAndGet(), UUID.randomUUID().toString()));
            } catch (Exception e) {
                return getResponse(Response.Status.INTERNAL_SERVER_ERROR, "Error calculating result: " + e.getMessage(), null);
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