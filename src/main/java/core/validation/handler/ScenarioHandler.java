package core.validation.handler;

import core.config.ConfigManager;
import core.scheduler.job.Job;
import core.scheduler.job.JobContainer;
import core.service.ServiceManager;
import core.util.FileManager;
import core.util.TimeUtil;
import core.validation.Validator;
import core.validation.model.ValidationModel;
import core.validation.scenario.Scenario;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ScenarioHandler extends JobContainer {

    private final Validator validator;
    private final List<String> discardKeywords;

    public ScenarioHandler(Validator validator, Job scenarioHandleJob, List<String> discardKeywords) {
        this.validator = validator;
        this.discardKeywords = discardKeywords;
        setJob(scenarioHandleJob);
    }

    public void start(Scenario scenario) {
        getJob().setRunnable(() -> {
            int wrongCount = 0;
            try {
                FileManager fileManager = scenario.getFileManager();
                ValidationModel actualModel = scenario.getActualModel();
                ValidationModel expectedModel = scenario.getExpectedModel();

                // 1) Get contents from scenario (expectedContent, actualContent)
                String actualFileName = actualModel.getFileName();
                if (!fileManager.trimFile(actualFileName)) {
                    log.warn("[{}] Fail to trim the file. ({})", scenario.getId(), actualFileName);
                    return;
                }

                String expectedFileName = expectedModel.getFileName();
                if (!fileManager.trimFile(expectedFileName)) {
                    log.warn("[{}] Fail to trim the file. ({})", scenario.getId(), expectedFileName);
                    return;
                }

                // 2) Compare line by line
                int expectedFileTotalLineNumber = fileManager.getTotalLineNumber(expectedFileName);
                int expectedLineNumber = 0;
                for (; expectedLineNumber <= expectedFileTotalLineNumber; expectedLineNumber++) {
                    scenario.setCurLineNumber(expectedLineNumber);

                    String expectedLine = fileManager.getLineByNumber(expectedLineNumber, expectedFileName);
                    String actualLine = fileManager.getLineByNumber(expectedLineNumber, actualFileName);

                    //log.debug("[{}] [A] [ {} ]", scenario.getId(), actualLine);
                    //log.debug("[{}] [E] [ {} ]", scenario.getId(), expectedLine);

                    // 1) actual: "@" & expected: null or "" > Wrong
                    if (fileManager.isEmptyString(actualLine) && !fileManager.isEmptyString(expectedLine)) {
                        wrongCount++;
                        continue;
                    }
                    // 2) actual: null or "" & expected: "#" > Wrong
                    else if (!fileManager.isEmptyString(actualLine) && fileManager.isEmptyString(expectedLine)) {
                        wrongCount++;
                        continue;
                    }
                    // 3) actual: "@" & expected: "#" > Check
                    else if (!fileManager.isEmptyString(actualLine) && !fileManager.isEmptyString(expectedLine)) {
                        // Check Discard Keyword
                        boolean isSkipped = false;
                        for (String discardKeyword : discardKeywords) {
                            if (actualLine.contains(discardKeyword)
                                    || expectedLine.contains(discardKeyword)) {
                                isSkipped = true;
                                break;
                            }
                        }
                        if (isSkipped) { continue; }

                        // Check equality
                        if (!actualLine.equals(expectedLine)) {
                            log.warn("[{}] [!]\n[ E({}): {} ] <> \n[ A({}): {} ]",
                                    scenario.getId(),
                                    expectedLineNumber, expectedLine,
                                    expectedLineNumber, actualLine
                            );
                            wrongCount++;
                        }
                    }

                    // File IO 과부하 방지
                    TimeUtil.sleep(10, TimeUnit.MILLISECONDS);
                }
            } catch (Exception e) {
                log.warn("[{}] [{}] Exception", scenario.getId(), getJob().getName(), e);
            } finally {
                // 3) Apply result
                scenario.setSuccess(wrongCount);
                if (validator.removeScenario(scenario.getId())) {
                    log.debug("[{}] [{}] Success to finish the scenario.", scenario.getId(), getJob().getName());
                }
            }
        });
    }

}
