package core.validation.model;

import core.util.FileManager;
import core.util.PrettyPrinter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ValidationResult {

    private final AtomicInteger totalScenarioCount = new AtomicInteger(0);
    private final Map<String, Object> results = new ConcurrentHashMap<>();

    public ValidationResult() {}

    public int getTotalScenarioCount() {
        return totalScenarioCount.get();
    }

    public void incAndGetTotalScenarioCount() {
        this.totalScenarioCount.incrementAndGet();
    }

    public void apply(String id, boolean isSuccess) {
        results.putIfAbsent(id, isSuccess);
    }

    public String getResults() {
        results.putIfAbsent("totalScenarioCount", getTotalScenarioCount());
        return PrettyPrinter.printJson(results);
    }

    public void save(String resultFilePath) {
        if (resultFilePath == null || resultFilePath.isEmpty()) { return; }

        FileManager fileManager = new FileManager();
        fileManager.writeString(resultFilePath, getResults(), false);
    }

}
