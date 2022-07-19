package core.validation;

import core.validation.config.ValidationConfig;
import core.validation.model.ValidationResult;
import core.validation.repository.ScenarioRepositoryFactory;
import core.validation.scenario.Scenario;

public class Validator {

    private final ScenarioRepositoryFactory scenarioRepository;
    private final ValidationResult validationResult;

    public Validator() {
        scenarioRepository = ValidationConfig.getScenarioRepository();
        validationResult = new ValidationResult();
    }

    public boolean addScenario(Scenario scenario) {
        return scenarioRepository.add(scenario);
    }

    public Scenario getScenario(String id) {
        return scenarioRepository.get(id);
    }

    public boolean removeScenario(String id) {
        return scenarioRepository.remove(id);
    }

    public void clear() {
        scenarioRepository.clear();
    }

    public int getTotalScenarioSize() {
        return scenarioRepository.size();
    }

    public ValidationResult getValidationResult() {
        return validationResult;
    }

}
