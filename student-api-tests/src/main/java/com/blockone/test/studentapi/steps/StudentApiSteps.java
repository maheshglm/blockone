package com.blockone.test.studentapi.steps;

import com.blockone.test.studentapi.mdl.RequestType;
import com.blockone.test.studentapi.mdl.Student;
import com.blockone.test.studentapi.svc.RestApiSvc;
import com.blockone.test.studentapi.svc.StateSvc;
import com.blockone.test.studentapi.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class StudentApiSteps {

    public static final String RECORD_COUNT = "RECORD_COUNT";
    @Autowired
    private RestApiSvc restApiSvc;

    @Autowired
    private JsonUtils jsonUtils;

    @Autowired
    private DatabaseSteps databaseSteps;

    @Autowired
    private StateSvc stateSvc;

    private static final String CONTENT_TYPE = "content-type";
    private static final String ADD_STUDENT_END_POINT = "/addStudent";
    private static final String SQL_TO_CHECK_STUDENT_ENTRY = """
            SELECT count(*) AS RECORD_COUNT FROM student 
                WHERE id=%s
                AND firstName='%s'
                AND lastName='%s' 
                AND class='%s'
                AND nationality='%s';
            """;

    private static final String SQL_TO_DELETE_STUDENT_ENTRY = """
            DELETE FROM student WHERE id IN (%s) 
            """;


    private void setDefaultHeaderParams() {
        Map<String, String> headers = new HashMap<>();
        headers.put(CONTENT_TYPE, String.valueOf(MediaType.APPLICATION_JSON));
        restApiSvc.setHeaderParams(headers);
    }

    private void setApiEndPoint(final String endPoint) {
        restApiSvc.setApiEndPoint(endPoint);
    }

    public void addStudent(Student student) {
        setApiEndPoint(ADD_STUDENT_END_POINT);
        setDefaultHeaderParams();
        final String body = stateSvc.expandExpression(jsonUtils.getJsonStringFromPojo(student));
        restApiSvc.setBodyParam(body);
        restApiSvc.sendRequest(RequestType.POST);
    }

    public void assertStudentEntry(Student student) {
        final String formattedSql = stateSvc.expandExpression(
                SQL_TO_CHECK_STUDENT_ENTRY.formatted(student.getId(),
                        student.getFirstName(),
                        student.getLastName(),
                        student.getClazz(),
                        student.getNationality()));

        databaseSteps.iExpectValueOfColumnShouldMatch(RECORD_COUNT,
                "1", formattedSql);
    }

    public void deleteStudent(int id) {
        final String formattedSql = stateSvc.expandExpression(
                SQL_TO_DELETE_STUDENT_ENTRY.formatted(id));
        databaseSteps.executeQuery(formattedSql);
    }

}
