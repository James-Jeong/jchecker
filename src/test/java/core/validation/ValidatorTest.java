package core.validation;

import core.config.ConfigManager;
import core.service.ServiceManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

@Slf4j
public class ValidatorTest {

    private static final String TEST_CONFIG_PATH = System.getProperty("user.dir")
            + "/src/test/resources/config/user_conf.ini";
    private static final String TEST_SCENARIO_PATH = System.getProperty("user.dir")
            + "/src/test/resources/scenario/aiccpgw_test.json";

    @Test
    public void test() {
        ServiceManager serviceManager = new ServiceManager(new ConfigManager(TEST_CONFIG_PATH));
        serviceManager.register(TEST_SCENARIO_PATH);
        Assert.assertTrue(serviceManager.start());
    }

}
