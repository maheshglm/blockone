package com.blockone.test.studentapi.svc;

import com.blockone.test.studentapi.CustomException;
import com.blockone.test.studentapi.mdl.RequestType;
import com.cfg.SpringConfig;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import io.restassured.http.ContentType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;

import static com.blockone.test.studentapi.svc.RestApiSvc.ENDPOINT_IS_NOT_SET_CANNOT_PROCESS_REQUEST;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringConfig.class)
public class RestApiSvcTest {

    private static final String HTTP_LOCALHOST = "http://localhost:";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String APPLICATION_JSON = "application/json";

    @Autowired
    private RestApiSvc restApiSvc;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(wireMockConfig().dynamicPort());

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setApiBaseUri() {
        restApiSvc.setApiBaseUri(HTTP_LOCALHOST + wireMockRule.port());
    }

    @Test
    public void testSendRequest_GET() {
        stubFor(get(urlPathMatching("/mockApi/get"))
                .withQueryParam("search_term", equalTo("WireMock"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(CONTENT_TYPE, String.valueOf(ContentType.JSON))
                        .withBodyFile("/mock_response.json")));

        restApiSvc.setApiEndPoint("/mockApi/get");
        restApiSvc.setEndPointParamsVar(new HashMap<>() {
            {
                put("search_term", "WireMock");
            }
        });

        restApiSvc.setHeaderParams(new HashMap<>() {{
            put(CONTENT_TYPE, APPLICATION_JSON);
        }});

        restApiSvc.sendRequest(RequestType.GET);

        Assert.assertEquals(200, restApiSvc.getStatusCode().intValue());

        Object method = restApiSvc.getJsonObjectFromResponse("request.method");
        Object url = restApiSvc.getJsonObjectFromResponse("request.url");

        Assert.assertEquals("GET", method.toString());
        Assert.assertEquals("localhost:8080", url.toString());
    }

    @Test
    public void testSendRequest_POST() {
        stubFor(post(urlPathMatching("/mockApi/post"))
                .withHeader(CONTENT_TYPE, containing(APPLICATION_JSON))
                .withRequestBody(equalToJson("{ \"status\" : \"OK\"}"))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withBody("Created"))
        );

        restApiSvc.setApiEndPoint("/mockApi/post");
        restApiSvc.setBodyParam("{ \"status\" : \"OK\"}");
        restApiSvc.setHeaderParams(new HashMap<>() {{
            put(CONTENT_TYPE, APPLICATION_JSON);
        }});

        restApiSvc.sendRequest(RequestType.POST);

        Assert.assertEquals(201, restApiSvc.getStatusCode().intValue());
        Assert.assertEquals("Created", restApiSvc.getResponseAsString());
    }

    @Test
    public void testSendRequest_DELETE() {
        stubFor(delete(urlEqualTo("/mockApi/delete"))
                .withHeader(CONTENT_TYPE, containing(APPLICATION_JSON))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("Deleted")));

        restApiSvc.setApiEndPoint("/mockApi/delete");
        restApiSvc.setHeaderParams(new HashMap<>() {{
            put(CONTENT_TYPE, APPLICATION_JSON);
        }});
        restApiSvc.sendRequest(RequestType.DELETE);

        Assert.assertEquals(200, restApiSvc.getStatusCode().intValue());
        Assert.assertEquals("Deleted", restApiSvc.getResponseAsString());
    }

    @Test
    public void testSendRequest_endPointIsNotSet() {
        thrown.expect(CustomException.class);
        thrown.expectMessage(ENDPOINT_IS_NOT_SET_CANNOT_PROCESS_REQUEST);

        stubFor(delete(urlEqualTo("/mockApi/delete/WireMock"))
                .withHeader(CONTENT_TYPE, containing(APPLICATION_JSON))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("Deleted")));

        restApiSvc.setApiEndPoint("");
        restApiSvc.sendRequest(RequestType.DELETE);
    }

    @Test
    public void testSendRequest_requestNotImplemented() {
        thrown.expect(CustomException.class);
        thrown.expectMessage("Request type [HEAD] is not implemented");
        stubFor(head(urlEqualTo("/mockApi/WireMock"))
                .withHeader(CONTENT_TYPE, containing(APPLICATION_JSON))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("Deleted")));

        restApiSvc.setApiEndPoint("/mockApi/WireMock");
        restApiSvc.sendRequest(RequestType.HEAD);
    }

}