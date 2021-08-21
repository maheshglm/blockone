package glue;

import com.blockone.test.studentapi.Bootstrap;
import com.blockone.test.studentapi.steps.RestApiSteps;
import com.blockone.test.studentapi.svc.DataTableSvc;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.cucumber.datatable.DataTable;

/**
 * Rest api steps def.
 */
public class RestApiStepsDef {

    RestApiSteps restApiSteps = (RestApiSteps) Bootstrap.getBean(RestApiSteps.class);

    DataTableSvc dataTableSvc = (DataTableSvc) Bootstrap.getBean(DataTableSvc.class);

    /**
     * Sets api end point.
     *
     * @param endPoint the end point
     */
    @When("end point configured as {string}")
    public void setApiEndPoint(final String endPoint) {
        restApiSteps.setApiEndPoint(endPoint);
    }

    /**
     * Sets body parameter.
     *
     * @param docString the doc string
     */
    @When("body parameter configured as")
    public void setBodyParameter(final String docString) {
        restApiSteps.setBodyParam(docString);
    }

    /**
     * Send post request.
     */
    @When("I send POST request")
    public void sendPostRequest() {
        restApiSteps.sendApiPostRequest();
    }

    /**
     * Send delete request.
     */
    @When("I send DELETE request")
    public void sendDeleteRequest() {
        restApiSteps.sendApiDeleteRequest();
    }

    /**
     * Send get request.
     */
    @When("I send GET request")
    public void sendGetRequest() {
        restApiSteps.sendApiGetRequest();
    }

    /**
     * Send put request.
     */
    @When("I send PUT request")
    public void sendPutRequest() {
        restApiSteps.sendApiPutRequest();
    }

    /**
     * Configure header params.
     *
     * @param dataTable the data table
     */
    @When("header params configured as")
    public void configureHeaderParams(final DataTable dataTable) {
        restApiSteps.setHeaderParams(dataTableSvc.getTwoColumnsAsMap(dataTable));
    }

    /**
     * Configure query params.
     *
     * @param dataTable the data table
     */
    @Given("query params configured as")
    public void configureQueryParams(final DataTable dataTable) {
        restApiSteps.setQueryParams(dataTableSvc.getTwoColumnsAsMap(dataTable));
    }

    /**
     * Expect status code.
     *
     * @param statusCode the status code
     */
    @Then("I expect {int} status code")
    public void expectStatusCode(final Integer statusCode) {
        restApiSteps.assertStatusCode(statusCode);
    }

    /**
     * Expect response string as.
     *
     * @param responseString the response string
     */
    @Then("I expect a response as {string}")
    public void expectResponseStringAs(final String responseString) {
        restApiSteps.assertResponseString(responseString);
    }

    /**
     * Expect response key values.
     *
     * @param dataTable the data table
     */
    @Then("I expect response including following json key values")
    public void expectResponseKeyValues(final DataTable dataTable) {
        restApiSteps.assertJsonResponse(dataTableSvc.getTwoColumnsAsMap(dataTable));
    }

}
