package glue;

import com.blockone.test.studentapi.Bootstrap;
import com.blockone.test.studentapi.steps.ConfigSteps;
import cucumber.api.java.en.Given;


/**
 * Config steps def.
 */
public class ConfigStepsDef {

    ConfigSteps configSteps = (ConfigSteps) Bootstrap.getBean(ConfigSteps.class);

    /**
     * Assign random number to variable.
     *
     * @param varName the var name
     */
    @Given("assign a random integer to variable named {string}")
    public void assignRandomNumberToVariable(String varName) {
        String random = configSteps.generateRandomInteger();
        configSteps.assignValueToVar(random, varName);
    }

}