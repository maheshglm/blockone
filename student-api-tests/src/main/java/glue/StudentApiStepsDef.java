package glue;

import com.blockone.test.studentapi.Bootstrap;
import com.blockone.test.studentapi.mdl.Student;
import com.blockone.test.studentapi.steps.StudentApiSteps;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class StudentApiStepsDef {

    private final StudentApiSteps studentApiSteps =
            (StudentApiSteps) Bootstrap.getBean(StudentApiSteps.class);

    @When("I add a student with below attributes")
    public void addStudent(Student student) {
        studentApiSteps.addStudent(student);
    }

    @Then("I expect there is a student entry in the database")
    public void assertDbStudentEntry(Student student) {
        studentApiSteps.assertStudentEntry(student);
    }

    @And("delete student with id {int}")
    public void deleteStudentRecord(int id) {
        studentApiSteps.deleteStudent(id);
    }

}
