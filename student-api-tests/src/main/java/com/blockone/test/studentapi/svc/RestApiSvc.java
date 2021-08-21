package com.blockone.test.studentapi.svc;

import com.blockone.test.studentapi.CustomException;
import com.blockone.test.studentapi.CustomExceptionType;
import com.blockone.test.studentapi.mdl.RequestType;
import com.google.common.base.Strings;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

@Service
public class RestApiSvc {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestApiSvc.class);
    public static final String API_RESPONSE_IS_NULL = "Api response is null";
    public static final String ENDPOINT_IS_NOT_SET_CANNOT_PROCESS_REQUEST = "Endpoint is not set, cannot process request";
    public static final String RESPONSE_IS_EMPTY = "Response is Empty";

    private ThreadLocal<Response> response = new ThreadLocal<>();
    private ThreadLocal<RequestSpecification> requestSpec = new ThreadLocal<>();
    private ThreadLocal<String> endPoint = new ThreadLocal<>();
    private ThreadLocal<Map<String, String>> endPointParams = ThreadLocal.withInitial(HashMap::new);
    private ThreadLocal<String> apiBaseUri = new ThreadLocal<>();

    //wrapper method on API interactions
    public Response sendRequest(final RequestType requestType) {
        try {
            if (Strings.isNullOrEmpty(endPoint.get())) {
                LOGGER.error(ENDPOINT_IS_NOT_SET_CANNOT_PROCESS_REQUEST);
                throw new CustomException(CustomExceptionType.UNDEFINED, ENDPOINT_IS_NOT_SET_CANNOT_PROCESS_REQUEST);
            }

            final String url = apiBaseUri.get() + endPoint.get();
            switch (requestType) {
                case GET:
                    response.set(this.sendGetRequest(url));
                    break;

                case POST:
                    response.set(this.sendPostRequest(url));
                    break;

                case DELETE:
                    response.set(this.sendDeleteRequest(url));
                    break;

                case PUT:
                    response.set(this.sendPutRequest(url));
                    break;

                default:
                    LOGGER.error("Request type [{}] is not implemented", requestType);
                    throw new CustomException(CustomExceptionType.UNDEFINED, "Request type [{}] is not implemented", requestType);
            }

            response.get().then().log().ifError();
            return response.get();
        } finally {
            this.clearRequestSpecifications();
        }
    }


    public void clearRequestSpecifications() {
        LOGGER.debug("Clearing Request Specifications");
        requestSpec = new ThreadLocal<>();
    }

    public RequestSpecification getRequestSpec() {
        return requestSpec.get();
    }

    public Response getResponse() {
        if (null == response) {
            LOGGER.error(RESPONSE_IS_EMPTY);
            throw new CustomException(CustomExceptionType.IO_ERROR, RESPONSE_IS_EMPTY);
        }
        return response.get();
    }


    public void setApiBaseUri(final String apiBaseUri) {
        LOGGER.debug("Setting Api base url [{}]", apiBaseUri);
        this.apiBaseUri.set(apiBaseUri);
    }

    public void setApiEndPoint(final String apiEndPoint) {
        LOGGER.debug("Setting Api endpoint [{}]", apiEndPoint);
        this.endPoint.set(apiEndPoint);
    }

    public void setEndPointParamsVar(final Map<String, String> params) {
        LOGGER.debug("Setting Api endpoint params [{}]", params);
        this.endPointParams.set(params);
    }

    //Setting header params
    public RequestSpecification setHeaderParams(final Map<String, String> headerParams) {
        LOGGER.debug("Setting Api header params [{}]", headerParams.toString());
        if (this.getRequestSpec() == null) {
            requestSpec.set(given().headers(headerParams));
        } else {
            requestSpec.set(this.getRequestSpec().given().headers(headerParams));
        }
        return requestSpec.get();
    }

    //Setting body params
    public RequestSpecification setBodyParam(final String bodyContent) {
        LOGGER.debug("Setting body param [{}]", bodyContent);
        if (this.getRequestSpec() == null) {
            requestSpec.set(given().body(bodyContent));
        } else {
            requestSpec.set(this.getRequestSpec().given().body(bodyContent));
        }
        return requestSpec.get();
    }


    private Response sendPostRequest(final String url) {
        if (this.getRequestSpec() == null) {
            return given()
                    .when()
                    .post(url);
        }
        return this.getRequestSpec()
                .when()
                .post(url);
    }

    private Response sendGetRequest(final String url) {
        if (this.getRequestSpec() == null) {
            return given()
                    .params(endPointParams.get())
                    .when()
                    .get(url);
        }
        return this.getRequestSpec()
                .given()
                .params(endPointParams.get())
                .when()
                .get(url);
    }

    private Response sendDeleteRequest(final String url) {
        if (this.getRequestSpec() == null) {
            return given()
                    .params(endPointParams.get())
                    .when()
                    .delete(url);
        }
        return this.getRequestSpec()
                .params(endPointParams.get())
                .when()
                .delete(url);
    }


    private Response sendPutRequest(final String url) {
        if (this.getRequestSpec() == null) {
            return given()
                    .when()
                    .put(url);
        }
        return this.getRequestSpec()
                .when()
                .put(url);
    }

    public Integer getStatusCode() {
        return getResponse().getStatusCode();
    }

    public String getResponseAsString() {
        return getResponse().asString();
    }

    public Object getJsonObjectFromResponse(final Response response, final String jsonKey) {
        if (response == null) {
            LOGGER.error(API_RESPONSE_IS_NULL);
            throw new CustomException(CustomExceptionType.UNDEFINED, API_RESPONSE_IS_NULL);
        }
        Object object = response.jsonPath().get(jsonKey);
        LOGGER.debug("Response Object for JsonKey [{}] is [{}]", jsonKey, object);
        return object;
    }

    public Object getJsonObjectFromResponse(final String jsonKey) {
        return getJsonObjectFromResponse(getResponse(), jsonKey);
    }

    public String getValueFromResponse(final String jsonKey) {
        final String result = getJsonObjectFromResponse(jsonKey).toString();
        LOGGER.debug("JsonKey [{}]:Value [{}]", jsonKey, result);
        return result;
    }

}
