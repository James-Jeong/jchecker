package core.validation.config;

import core.validation.repository.ScenarioMap;
import core.validation.repository.ScenarioRepositoryFactory;

public class ValidationConfig {

    public static ScenarioRepositoryFactory getScenarioRepository() {
        return new ScenarioMap();
    }

}
