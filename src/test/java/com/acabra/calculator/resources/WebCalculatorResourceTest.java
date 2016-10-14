package com.acabra.calculator.resources;

import com.acabra.calculator.WebCalculatorManager;
import com.acabra.calculator.domain.IntegralRequest;
import com.acabra.calculator.integral.IntegralSubRangeSupplier;
import com.acabra.calculator.request.IntegralRequestDTO;
import com.acabra.calculator.response.*;
import com.acabra.calculator.util.JsonHelper;
import com.acabra.calculator.util.RequestMapper;
import com.acabra.calculator.view.WebCalculatorRendererHTML;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.assertj.core.api.Assertions;
import org.glassfish.jersey.test.grizzly.GrizzlyTestContainerFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
/**
 * Created by Agustin on 10/10/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({RequestMapper.class, WebCalculatorManager.class, WebCalculatorResource.class,
        IntegralSubRangeSupplier.class})
@PowerMockIgnore(value = {"javax.management.*"})
public class WebCalculatorResourceTest {

    private static final WebCalculatorManager calcManagerMock = PowerMockito.mock(WebCalculatorManager.class);

    private static final String TOKEN = "TOKEN";
    private static WebCalculatorResource webCalculatorResource = new WebCalculatorResource(calcManagerMock);
    private static CalculationResponse failedArithmeticResponse = new CalculationResponse(-1L, true, "", Double.toString(Double.NaN), -1L, "");
    private static IntegralCalculationResponse failedIntegralResponse = new IntegralCalculationResponse(-1L, true, "", Double.NaN, Double.NaN, -1L, "");
    private CalculationResponse arithmeticResponseStub = new CalculationResponse(1L, true, "expression", "0.0", 2L, "description");
    private IntegralCalculationResponse integralCalculationStub = new IntegralCalculationResponse(1L, false, "integralexpr", 9.0, 5.0, 3L, "descriptionIntegral");
    private CompletableFuture<CalculationResponse> integralFutureStub = CompletableFuture.completedFuture(integralCalculationStub);
    private IntegralRequestDTO integralRequestDTOStub = null;
    private IntegralRequest integralRequestStub = new IntegralRequest(0, 1, 1, 1, 0, 0, true, Collections.emptyList());


    @Rule
    public ResourceTestRule RULE = ResourceTestRule.builder()
            .setTestContainerFactory(new GrizzlyTestContainerFactory())
            .addResource(webCalculatorResource)
            .build();

    @Before
    public void setup() throws IOException {
    }

    @After
    public void tearDown() {
        Mockito.reset(calcManagerMock);
    }


    @Test
    public void makeCalculationTest() {
        when(calcManagerMock.processArithmeticCalculation(eq("expression"), eq(TOKEN))).thenReturn(arithmeticResponseStub);
        Response post = RULE.getJerseyTest()
                .target("/calculator")
                .queryParam("expression", "expression")
                .queryParam("token", TOKEN)
                .request(MediaType.APPLICATION_JSON).post(null);

        Assertions.assertThat(post.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        CalculationResponse calculationResponse = post.readEntity(new GenericType<MessageResponse<CalculationResponse>>(){}).getBody();

        Assertions.assertThat(calculationResponse.getId()).isNotEqualTo(failedArithmeticResponse.getId());
        Assertions.assertThat(calculationResponse.getId()).isEqualTo(1L);
        Assertions.assertThat(calculationResponse.getResponseTime()).isEqualTo(2L);
        Assertions.assertThat(calculationResponse.getExpression()).isEqualTo("expression");
        Assertions.assertThat(calculationResponse.getDescription()).isEqualTo("description");

        verify(calcManagerMock, times(1)).processArithmeticCalculation(eq("expression"), eq(TOKEN));
    }

    @Test
    public void makeCalculationFailOtherTest() {
        when(calcManagerMock.processArithmeticCalculation(anyString(), anyString())).thenThrow(NullPointerException.class);
        Response post = RULE.getJerseyTest()
                .target("/calculator")
                .queryParam("expression", "expression")
                .queryParam("token", TOKEN)
                .request(MediaType.APPLICATION_JSON).post(null);

        Assertions.assertThat(post.getStatus()).isEqualTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());

        verify(calcManagerMock, times(1)).processArithmeticCalculation(anyString(), anyString());
    }

    @Test
    public void resolveIntegralTest() {
        integralRequestDTOStub = JsonHelper.fromJsonString(fixture("stubs/integralRequestDTO.json"), IntegralRequestDTO.class).orElse(null);

        PowerMockito.mockStatic(RequestMapper.class);
        PowerMockito.when(RequestMapper.fromInternalRequest(anyObject())).thenReturn(integralRequestStub);

        when(calcManagerMock.processIntegralCalculation(anyObject(), anyString())).thenReturn(integralFutureStub);

        Response post = RULE.getJerseyTest()
                .target("/calculator/integral")
                .queryParam("token", TOKEN)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(integralRequestDTOStub));

        Assertions.assertThat(post.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());

        IntegralCalculationResponse integralCalculationResponse = post.readEntity(new GenericType<MessageResponse<IntegralCalculationResponse>>(){}).getBody();

        Assertions.assertThat(integralCalculationResponse.getId()).isNotEqualTo(failedIntegralResponse);
        Assertions.assertThat(integralCalculationResponse.getIntegralResult()).isEqualTo(integralCalculationStub.getIntegralResult());
        Assertions.assertThat(integralCalculationResponse.getAccuracy()).isEqualTo(integralCalculationStub.getAccuracy());
        Assertions.assertThat(integralCalculationResponse.getId()).isEqualTo(integralCalculationStub.getId());
        Assertions.assertThat(integralCalculationResponse.getResponseTime()).isEqualTo(integralCalculationStub.getResponseTime());
        Assertions.assertThat(integralCalculationResponse.getResult()).isEqualTo(integralCalculationStub.getResult());
        Assertions.assertThat(integralCalculationResponse.getDescription()).isEqualTo(integralCalculationStub.getDescription());
        Assertions.assertThat(integralCalculationResponse.getExpression()).isEqualTo(integralCalculationStub.getExpression());

        verify(calcManagerMock, times(1)).processIntegralCalculation(anyObject(), anyString());

        PowerMockito.verifyStatic(times(1));
        RequestMapper.fromInternalRequest(anyObject());
    }

    @Test
    public void resolveIntegralFailOtherTest() {
        integralRequestDTOStub = JsonHelper.fromJsonString(fixture("stubs/integralRequestDTO.json"), IntegralRequestDTO.class).orElse(null);

        PowerMockito.mockStatic(RequestMapper.class);
        PowerMockito.when(RequestMapper.fromInternalRequest(anyObject())).thenReturn(integralRequestStub);

        when(calcManagerMock.processIntegralCalculation(anyObject(), anyString())).thenThrow(NullPointerException.class);

        Response post = RULE.getJerseyTest()
                .target("/calculator/integral")
                .queryParam("token", TOKEN)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(integralRequestDTOStub));

        Assertions.assertThat(post.getStatus()).isEqualTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());

        verify(calcManagerMock, times(1)).processIntegralCalculation(anyObject(), anyString());
        PowerMockito.verifyStatic(times(1));
        RequestMapper.fromInternalRequest(anyObject());
    }

    @Test
    public void provideSessionTokenTest() {

        TokenResponse tokenResponseStub = new TokenResponse(1L, TOKEN);
        TokenResponse failedTokenResponse = new TokenResponse(-1L, "");

        when(calcManagerMock.provideSessionToken()).thenReturn(tokenResponseStub);

        Response post = RULE.getJerseyTest()
                .target("/calculator/token")
                .request(MediaType.APPLICATION_JSON)
                .post(null);

        Assertions.assertThat(post.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());

        TokenResponse tokenResponse = post.readEntity(new GenericType<MessageResponse<TokenResponse>>(){}).getBody();

        Assertions.assertThat(tokenResponse.getId()).isNotEqualTo(failedTokenResponse);
        Assertions.assertThat(tokenResponse.getToken()).isEqualTo(TOKEN);

        verify(calcManagerMock, times(1)).provideSessionToken();
    }

    @Test
    public void provideSessionTokenFailOtherTest() {

        when(calcManagerMock.provideSessionToken()).thenThrow(NullPointerException.class);

        Response post = RULE.getJerseyTest()
                .target("/calculator/token")
                .queryParam("token", TOKEN)
                .request(MediaType.APPLICATION_JSON)
                .post(null);

        Assertions.assertThat(post.getStatus()).isEqualTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());

        verify(calcManagerMock, times(1)).provideSessionToken();
    }

    @Test
    public void provideRenderedHistoryResultTest() {

        RenderedHistoryResponse failedHistoryResponseStub = new RenderedHistoryResponse(-1L, "");

        String renderedTable = "renderedTable";
        RenderedHistoryResponse historyResponseStub = new RenderedHistoryResponse(1L, renderedTable);
        when(calcManagerMock.provideRenderedHistoryResult(eq(TOKEN))).thenReturn(historyResponseStub);

        Response post = RULE.getJerseyTest()
                .target("/calculator/history")
                .queryParam("token", TOKEN)
                .request(MediaType.APPLICATION_JSON)
                .get();

        Assertions.assertThat(post.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());

        RenderedHistoryResponse tableHistoryResponse = post.readEntity(new GenericType<MessageResponse<RenderedHistoryResponse>>(){}).getBody();

        Assertions.assertThat(tableHistoryResponse.getId()).isNotEqualTo(failedHistoryResponseStub);
        Assertions.assertThat(tableHistoryResponse.getRenderedTable()).isEqualTo(renderedTable);

        verify(calcManagerMock, times(1)).provideRenderedHistoryResult(eq(TOKEN));
    }

    @Test
    public void provideRenderedHistoryResultFailNoTokenTest() {

        when(calcManagerMock.provideRenderedHistoryResult(anyString())).thenThrow(NoSuchElementException.class);

        Response post = RULE.getJerseyTest()
                .target("/calculator/history")
                .queryParam("token", TOKEN)
                .request(MediaType.APPLICATION_JSON)
                .get();

        Assertions.assertThat(post.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());

        verify(calcManagerMock, times(1)).provideRenderedHistoryResult(anyString());
    }

    @Test
    public void provideRenderedHistoryResultFailOtherTest() {

        when(calcManagerMock.provideRenderedHistoryResult(anyString())).thenThrow(NullPointerException.class);

        Response post = RULE.getJerseyTest()
                .target("/calculator/history")
                .queryParam("token", TOKEN)
                .request(MediaType.APPLICATION_JSON)
                .get();

        Assertions.assertThat(post.getStatus()).isEqualTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());

        verify(calcManagerMock, times(1)).provideRenderedHistoryResult(anyString());
    }
}
