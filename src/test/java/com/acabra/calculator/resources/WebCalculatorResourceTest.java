package com.acabra.calculator.resources;

import com.acabra.calculator.WebCalculatorManager;
import com.acabra.calculator.domain.IntegralRequest;
import com.acabra.calculator.request.IntegralRequestDTO;
import com.acabra.calculator.response.*;
import com.acabra.calculator.util.JsonHelper;
import com.acabra.calculator.util.RequestMapper;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.assertj.core.api.Assertions;
import org.glassfish.jersey.test.grizzly.GrizzlyTestContainerFactory;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
/**
 * Created by Agustin on 10/10/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({RequestMapper.class, WebCalculatorManager.class, WebCalculatorResource.class})
@PowerMockIgnore(value = {"javax.management.*"})
public class WebCalculatorResourceTest {

    private static final WebCalculatorManager calcManagerMock = PowerMockito.mock(WebCalculatorManager.class);

    private static final String TOKEN = "TOKEN";
    private static WebCalculatorResource webCalculatorResource = new WebCalculatorResource(calcManagerMock);
    private static CalculationResponse failedArithmeticResponse = new CalculationResponse(-1L, "", Double.NaN, -1L, "");
    private static IntegralCalculationResponse failedIntegralResponse = new IntegralCalculationResponse(-1L, "", Double.NaN, Double.NaN, -1L, "");
    private CalculationResponse arithmeticResponseStub = new CalculationResponse(1L, "expression", 0.0, 2L, "description");
    private IntegralCalculationResponse integralCalculationStub = new IntegralCalculationResponse(1L, "integralexpr", 9.0, 5.0, 3L, "descriptionIntegral");
    private CompletableFuture<CalculationResponse> integralFutureStub = CompletableFuture.completedFuture(integralCalculationStub);
    private IntegralRequestDTO integralRequestDTOStub = null;
    private IntegralRequest integralRequestStub = new IntegralRequest(0, 1, 1, 1, 0, 0, true);


    @Rule
    public ResourceTestRule RULE = ResourceTestRule.builder()
            .setTestContainerFactory(new GrizzlyTestContainerFactory())
            .addResource(webCalculatorResource)
            .build();

    @Before
    public void setup() throws IOException {
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

        String jsonBody = post.readEntity(MessageResponse.class).getBody();
        CalculationResponse calculationResponse = JsonHelper.fromJsonString(jsonBody, CalculationResponse.class).orElse(failedArithmeticResponse);

        Assertions.assertThat(calculationResponse.getId()).isNotEqualTo(failedArithmeticResponse.getId());
        Assertions.assertThat(calculationResponse.getId()).isEqualTo(1L);
        Assertions.assertThat(calculationResponse.getResponseTime()).isEqualTo(2L);
        Assertions.assertThat(calculationResponse.getExpression()).isEqualTo("expression");
        Assertions.assertThat(calculationResponse.getDescription()).isEqualTo("description");

        verify(calcManagerMock, times(1)).processArithmeticCalculation(eq("expression"), eq(TOKEN));
        Mockito.reset(calcManagerMock);
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
        Mockito.reset(calcManagerMock);
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

        MessageResponse messageResponse = post.readEntity(MessageResponse.class);
        String jsonBody = messageResponse.getBody();
        IntegralCalculationResponse integralCalculationResponse = JsonHelper.fromJsonString(jsonBody, IntegralCalculationResponse.class).orElse(failedIntegralResponse);

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
        Mockito.reset(calcManagerMock);
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
        Mockito.reset(calcManagerMock);
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

        TokenResponse tokenResponse = JsonHelper.fromJsonString(post.readEntity(MessageResponse.class).getBody(),
                TokenResponse.class).orElse(failedTokenResponse);

        Assertions.assertThat(tokenResponse.getId()).isNotEqualTo(failedTokenResponse);
        Assertions.assertThat(tokenResponse.getToken()).isEqualTo(TOKEN);

        verify(calcManagerMock, times(1)).provideSessionToken();
        Mockito.reset(calcManagerMock);
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
        Mockito.reset(calcManagerMock);
    }

    @Test
    public void provideRenderedHistoryResultTest() {

        TableHistoryResponse failedHistoryResponseStub = new TableHistoryResponse(-1L, "");

        String renderedTable = "renderedTable";
        TableHistoryResponse historyResponseStub = new TableHistoryResponse(1L, renderedTable);
        when(calcManagerMock.provideRenderedHistoryResult(eq(TOKEN))).thenReturn(historyResponseStub);

        Response post = RULE.getJerseyTest()
                .target("/calculator/history")
                .queryParam("token", TOKEN)
                .request(MediaType.APPLICATION_JSON)
                .get();

        Assertions.assertThat(post.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());

        TableHistoryResponse tableHistoryResponse = JsonHelper.fromJsonString(post.readEntity(MessageResponse.class).getBody(),
                TableHistoryResponse.class).orElse(failedHistoryResponseStub);

        Assertions.assertThat(tableHistoryResponse.getId()).isNotEqualTo(failedHistoryResponseStub);
        Assertions.assertThat(tableHistoryResponse.getTableHTML()).isEqualTo(renderedTable);

        verify(calcManagerMock, times(1)).provideRenderedHistoryResult(eq(TOKEN));
        Mockito.reset(calcManagerMock);
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
        Mockito.reset(calcManagerMock);
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
        Mockito.reset(calcManagerMock);
    }
}
