package core;

import core.config.ConfigManager;
import core.service.ServiceManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UCheckMain {

    public static void main(String[] args) {
        try {
            ////////////////////////////////////////////////////////////////////////////////
            // 1) Get arguments
            if (args.length != 3) {
                log.error("Argument Error. (&0: Main, &1: config_path,  &2: scenario_path)");
                return;
            }

            String configPath = args[1].trim();
            log.info("[UCheckMain] Config path: {}", configPath);
            if (configPath.isEmpty()) {
                log.error("Config path is empty. Fail to start.");
                return;
            }

            String scenarioPath = args[2].trim();
            log.info("[UCheckMain] ScenarioPath: {}", scenarioPath);
            if (scenarioPath.isEmpty()) {
                log.error("ScenarioPath is empty. Fail to start.");
                return;
            }
            ////////////////////////////////////////////////////////////////////////////////

            ////////////////////////////////////////////////////////////////////////////////
            // 2) Apply to service
            ServiceManager serviceManager = new ServiceManager(new ConfigManager(configPath));
            serviceManager.register(scenarioPath);
            serviceManager.start();
            ////////////////////////////////////////////////////////////////////////////////
        } catch (Exception e) {
            log.error("UCheckMain.main.Exception", e);
        }
    }

}
