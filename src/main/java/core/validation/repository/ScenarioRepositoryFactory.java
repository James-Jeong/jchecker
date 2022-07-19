package core.validation.repository;

import core.validation.scenario.Scenario;

public interface ScenarioRepositoryFactory {

    boolean add(Scenario scenario);
    Scenario get(String id);
    boolean remove(String id);
    void clear();
    int size();

}
