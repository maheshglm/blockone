package glue;

import com.blockone.test.studentapi.Bootstrap;
import com.blockone.test.studentapi.steps.HooksSteps;
import cucumber.api.java.Before;

/**
 * Cucumber Hooks.
 */
public class Hooks {

    private HooksSteps hooksSteps = (HooksSteps) Bootstrap.getBean(HooksSteps.class);

    /**
     * Is application running.
     */
    @Before(order = 1)
    public void isApplicationRunning() {
        hooksSteps.isApplicationRunning();
    }

    /**
     * Connect database.
     */
    @Before(order = 2)
    public void connectDatabase() {
        hooksSteps.connectToDatabase();
    }


}
