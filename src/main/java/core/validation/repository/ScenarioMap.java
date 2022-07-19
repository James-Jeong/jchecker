package core.validation.repository;

import core.validation.scenario.Scenario;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ScenarioMap implements ScenarioRepositoryFactory{

    private final Map<String, Scenario> scenarioMap;

    public ScenarioMap() {
        this.scenarioMap = new ConcurrentHashMap<>();
    }

    public boolean add(Scenario scenario) {
        if (scenario == null) {
            log.warn("Fail to add the scenario. Scenario is null.");
            return false;
        }

        Scenario result = scenarioMap.putIfAbsent(scenario.getId(), scenario);
        if (result == null) { // 처음 추가
            return scenario.start();
        }

        return false; // null 이 아니면 기존에 존재했다는 의미이므로 false 반환
    }

    public Scenario get(String id) {
        if (id == null || id.isEmpty()) { return null; }

        return scenarioMap.get(id);
    }

    public boolean remove(String id) {
        if (id == null || id.isEmpty()) { return false; }

        Scenario scenario = scenarioMap.remove(id);
        if (scenario == null) {
            return false;
        } else {
            scenario.stop();
            return true;
        }
    }

    @Override
    public void clear() {
        scenarioMap.clear();
    }

    @Override
    public int size() {
        return scenarioMap.size();
    }

}
