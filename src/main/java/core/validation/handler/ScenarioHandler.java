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
            try {
                FileManager fileManager = scenario.getFileManager();
                ValidationModel actualModel = scenario.getActualModel();
                ValidationModel expectedModel = scenario.getExpectedModel();

                // 1) Get contents from scenario (expectedContent, actualContent)
                String actualFileName = actualModel.getFileName();
                String expectedFileName = expectedModel.getFileName();

                // 2) Compare them line by line
                // 2-1) Check total line number
                int actualFileTotalLineNumber = fileManager.getTotalLineNumber(actualFileName);

                // 2-2) Check lines
                int wrongCount = 0;
                int lineNumber = 1;
                for (; lineNumber <= actualFileTotalLineNumber; lineNumber++) {
                    scenario.setCurLineNumber(lineNumber);

                    String actualLine = fileManager.getLineByNumber(lineNumber, actualFileName);
                    boolean isSkipped = false;
                    for (String discardKeyword : discardKeywords) {
                        if (actualLine.contains(discardKeyword)) {
                            isSkipped = true;
                            break;
                        }
                    }
                    if (isSkipped) { continue; }

                    String expectedLine = fileManager.getLineByNumber(lineNumber, expectedFileName);

                    log.debug("----------------------");
                    log.debug("[{}] [A] [ {} ]", scenario.getId(), actualLine);
                    log.debug("[{}] [E] [ {} ]", scenario.getId(), expectedLine);

                    // 1) actual: null or "" & expected: "~"
                    if ((actualLine == null || actualLine.isEmpty())
                            && (expectedLine != null && !expectedLine.isEmpty())) {
                        wrongCount++;
                    }
                    // 2) actual: "~" & expected: null or ""
                    else if ((actualLine != null && !actualLine.isEmpty())
                            && (expectedLine == null || expectedLine.isEmpty())) {
                        wrongCount++;
                    }
                    // 3) actual: "@" & expected: "#"
                    else if (actualLine != null && !actualLine.equals(expectedLine)) {
                        log.warn("[{}] [!]\n[ E: {} ] <> \n[ A: {} ]", scenario.getId(), expectedLine, actualLine);
                        wrongCount++;
                    }

                    // File IO 과부하 방지
                    TimeUtil.sleep(10, TimeUnit.MILLISECONDS);
                }

                // 3) Apply result
                scenario.setSuccess(wrongCount);
            } catch (Exception e) {
                log.warn("[{}] [{}] Exception", scenario.getId(), getJob().getName(), e);
            } finally {
                if (validator.removeScenario(scenario.getId())) {
                    log.debug("[{}] [{}] Success to finish the scenario.", scenario.getId(), getJob().getName());
                }
            }
        });
    }

}
