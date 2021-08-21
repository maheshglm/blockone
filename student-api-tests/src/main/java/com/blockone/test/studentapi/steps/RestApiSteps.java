package com.blockone.test.studentapi.steps;

import com.blockone.test.studentapi.CustomException;
import com.blockone.test.studentapi.CustomExceptionType;
import com.blockone.test.studentapi.mdl.RequestType;
import com.blockone.test.studentapi.svc.RestApiSvc;
import com.blockone.test.studentapi.svc.StateSvc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * Rest api steps.
 */
@Component
public class RestApiSteps {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestApiSteps.class);

    private static final String EXPECTED_JSON_KEY_VALUE_IS_AND_ACTUAL_VALUE_IS = "Expected Json Key [{}] value is [{}] and Actual value is [{}]";
    private static final String ACTUAL_RESPONSE_BODY_DOES_NOT_CONTAIN = "Actual Response Body does not contain [{}]";
    private static final String EXPECTED_STATUS_CODE_BUT_ACTUAL_STATUS_CODE_IS = "Expected Status Code [{}], but Actual Status Code is [{}]";
    private static final String APPLICATION_ON_IS_NOT_RUNNING = "Application on [{}] is not running";

    @Autowired
    private StateSvc stateSvc;

    @Autowired
    private RestApiSvc restApiSvc;

    /**
     * Gets app url.
     *
     * @param urlVar the url var
     * @return the app url
     */
    public String getAppUrl(final String urlVar) {
        return stateSvc.getStringVar(urlVar);
    }

    /**
     * Check api running.
     *
     * @param urlVar the url var
     */
    public void checkApiRunning(final String urlVar) {
        final String apiBaseUrl = getAppUrl(urlVar);
        try {
            URL myURL = new URL(apiBaseUrl);
            URLConnection myURLConnection = myURL.openConnection();
            myURLConnection.connect();
            restApiSvc.setApiBaseUri(apiBaseUrl);
        } catch (IOException e) {
            LOGGER.error(APPLICATION_ON_IS_NOT_RUNNING, apiBaseUrl);
            throw new CustomException(CustomExceptionType.IO_ERROR, APPLICATION_ON_IS_NOT_RUNNING, apiBaseUrl);
        }
    }

    /**
     * Sets api end point.
     *
     * @param endPoint the end point
     */
    public void setApiEndPoint(final String endPoint) {
        final String expandApiEndPoint = stateSvc.expandExpression(endPoint);
        restApiSvc.setApiEndPoint(expandApiEndPoint);
    }

    /**
     * Sets body param.
     *
     * @param bodyParam the body param
     */
    public void setBodyParam(final String bodyParam) {
        final String expandBodyContext = stateSvc.expandExpression(bodyParam);
        restApiSvc.setBodyParam(expandBodyContext);
    }

    /**
     * Sets header params.
     *
     * @param headerParams the header params
     */
    public void setHeaderParams(final Map<String, String> headerParams) {
        restApiSvc.setHeaderParams(headerParams);
    }

    /**
     * Sets query params.
     *
     * @param queryParams the query params
     */
    public void setQueryParams(final Map<String, String> queryParams) {
        Map<String, String> localMap = new HashMap<>(queryParams);
        localMap.forEach((key, value) -> localMap.replace(key, stateSvc.expandExpression(value)));
        restApiSvc.setEndPointParamsVar(localMap);
    }

    /**
     * Send api post request.
     */
    public void sendApiPostRequest() {
        restApiSvc.sendRequest(RequestType.POST);
    }

    /**
     * Send api delete request.
     */
    public void sendApiDeleteRequest() {
        restApiSvc.sendRequest(RequestType.DELETE);
    }

    /**
     * Send api get request.
     */
    public void sendApiGetRequest() {
        restApiSvc.sendRequest(RequestType.GET);
    }

    /**
     * Send api put request.
     */
    public void sendApiPutRequest() {
        restApiSvc.sendRequest(RequestType.PUT);
    }

    /**
     * Assert status code.
     *
     * @param expectedStatusCode the expected status code
     */
    public void assertStatusCode(final Integer expectedStatusCode) {
        final Integer actualStatusCode = restApiSvc.getStatusCode();
        LOGGER.debug("Actual Status Code is [{}]", actualStatusCode);

        if (!expectedStatusCode.equals(actualStatusCode)) {
            LOGGER.error(EXPECTED_STATUS_CODE_BUT_ACTUAL_STATUS_CODE_IS, expectedStatusCode, actualStatusCode);
            throw new CustomException(CustomExceptionType.VERIFICATION_FAILED, EXPECTED_STATUS_CODE_BUT_ACTUAL_STATUS_CODE_IS, expectedStatusCode, actualStatusCode);
        }
    }

    /**
     * Assert response string.
     *
     * @param responseAsString the response as string
     */
    public void assertResponseString(final String responseAsString) {
        final String expandExpectedResponse = stateSvc.expandExpression(responseAsString);
        final String actualResponse = restApiSvc.getResponseAsString();

        LOGGER.debug("Actual Response is [{}]", actualResponse);
        if (!actualResponse.contains(expandExpectedResponse)) {
            LOGGER.error(ACTUAL_RESPONSE_BODY_DOES_NOT_CONTAIN, expandExpectedResponse);
            throw new CustomException(CustomExceptionType.VERIFICATION_FAILED, ACTUAL_RESPONSE_BODY_DOES_NOT_CONTAIN, expandExpectedResponse);
        }
    }

    /**
     * Assert json response.
     *
     * @param jsonKeyValueMap the json key value map
     */
    public void assertJsonResponse(final Map<String, String> jsonKeyValueMap) {
        for (String jsonKey : jsonKeyValueMap.keySet()) {
            final String actualValue = restApiSvc.getValueFromResponse(jsonKey);
            final String expectedValue = stateSvc.expandExpression(jsonKeyValueMap.get(jsonKey));
            if (!actualValue.contains(expectedValue)) {
                LOGGER.error(EXPECTED_JSON_KEY_VALUE_IS_AND_ACTUAL_VALUE_IS, jsonKey, expectedValue, actualValue);
                throw new CustomException(CustomExceptionType.VERIFICATION_FAILED, EXPECTED_JSON_KEY_VALUE_IS_AND_ACTUAL_VALUE_IS, jsonKey, expectedValue, actualValue);
            }
        }
    }

}
