package core.scenario;

import core.util.ScenarioParser;
import core.validation.scenario.model.ScenarioDto;
import org.junit.Assert;
import org.junit.Test;

public class ScenarioTest {

    private static final String TEST_SCENARIO_PATH = System.getProperty("user.dir") + "/src/test/resources/scenario/aiccpgw_test.json";

    @Test
    public void parseTest() {
        ScenarioParser scenarioParser = new ScenarioParser(TEST_SCENARIO_PATH);
        ScenarioDto scenarioDto = scenarioParser.readScenarioDto();
        Assert.assertNotNull(scenarioDto);
    }

}
