$(document).ready(function() {var formatter = new CucumberHTML.DOMFormatter($('.cucumber-report'));formatter.uri("file:tests/features/DeleteStudent.feature");
formatter.feature({
  "name": "Delete student with /deleteStudent endpoint",
  "description": "  As a Student-api user,\n  I expect DELETE request with valid id on /deleteStudent endpoint should result in deleting student from database",
  "keyword": "Feature",
  "tags": [
    {
      "name": "@student-api"
    },
    {
      "name": "@delete-student"
    }
  ]
});
formatter.scenario({
  "name": "Delete student",
  "description": "  application should allow deleting a valid student",
  "keyword": "Scenario",
  "tags": [
    {
      "name": "@student-api"
    },
    {
      "name": "@delete-student"
    }
  ]
});
formatter.before({
  "status": "passed"
});
formatter.before({
  "status": "passed"
});
formatter.step({
  "name": "assign a random integer to variable named \"random_id\"",
  "keyword": "* "
});
formatter.match({
  "location": "ConfigStepsDef.assignRandomNumberToVariable(String)"
});
formatter.result({
  "status": "passed"
});
formatter.step({
  "name": "I execute below query to \"insert a student record\"",
  "keyword": "* ",
  "doc_string": {
    "value": "INSERT INTO student (id, firstName, lastName, class, nationality)\nVALUES (${random_id}, \u0027Delete\u0027, \u0027User\u0027, \u00271 C\u0027, \u0027SG\u0027);"
  }
});
formatter.match({
  "location": "DatabaseStepsDef.executeQuery(String,String)"
});
formatter.result({
  "status": "passed"
});
formatter.step({
  "name": "end point configured as \"/deleteStudent\"",
  "keyword": "Given "
});
formatter.match({
  "location": "RestApiStepsDef.setApiEndPoint(String)"
});
formatter.result({
  "status": "passed"
});
formatter.step({
  "name": "query params configured as",
  "rows": [
    {
      "cells": [
        "id",
        "${random_id}"
      ]
    }
  ],
  "keyword": "And "
});
formatter.match({
  "location": "RestApiStepsDef.configureQueryParams(DataTable)"
});
formatter.result({
  "status": "passed"
});
formatter.step({
  "name": "header params configured as",
  "rows": [
    {
      "cells": [
        "content-type",
        "application/json"
      ]
    }
  ],
  "keyword": "And "
});
formatter.match({
  "location": "RestApiStepsDef.configureHeaderParams(DataTable)"
});
formatter.result({
  "status": "passed"
});
formatter.step({
  "name": "I send DELETE request",
  "keyword": "When "
});
formatter.match({
  "location": "RestApiStepsDef.sendDeleteRequest()"
});
formatter.result({
  "status": "passed"
});
formatter.step({
  "name": "I expect 200 status code",
  "keyword": "Then "
});
formatter.match({
  "location": "RestApiStepsDef.expectStatusCode(Integer)"
});
formatter.result({
  "status": "passed"
});
formatter.step({
  "name": "I expect value of column \"RECORD_COUNT\" in the below query equals to \"0\"",
  "keyword": "And ",
  "doc_string": {
    "value": "SELECT count(*) AS RECORD_COUNT FROM student\nWHERE id\u003d${random_id}\nAND firstName\u003d\u0027Delete\u0027\nAND lastName\u003d\u0027User\u0027\nAND class\u003d\u00271 C\u0027\nAND nationality\u003d\u0027UK\u0027;"
  }
});
formatter.match({
  "location": "DatabaseStepsDef.verifyColumnValue(String,String,String)"
});
formatter.result({
  "status": "passed"
});
formatter.scenario({
  "name": "Cleanup",
  "description": "",
  "keyword": "Scenario",
  "tags": [
    {
      "name": "@student-api"
    },
    {
      "name": "@delete-student"
    }
  ]
});
formatter.before({
  "status": "passed"
});
formatter.before({
  "status": "passed"
});
formatter.step({
  "name": "I execute below query to \"delete students created above\"",
  "keyword": "* ",
  "doc_string": {
    "value": "DELETE FROM student WHERE id IN (${random_id});"
  }
});
formatter.match({
  "location": "DatabaseStepsDef.executeQuery(String,String)"
});
formatter.result({
  "status": "passed"
});
});