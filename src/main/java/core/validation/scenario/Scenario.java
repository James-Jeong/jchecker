package core.validation.scenario;

import core.scheduler.job.Job;
import core.scheduler.job.JobBuilder;
import core.scheduler.schedule.ScheduleManager;
import core.util.FileManager;
import core.validation.Validator;
import core.validation.handler.ScenarioHandler;
import core.validation.model.ValidationModel;
import core.validation.model.ValidationModelBuilder;
import core.validation.scenario.model.CaseDto;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class Scenario {

    private final Validator validator;
    private final String id;
    private final FileManager fileManager;
    private final String scheduleKey;

    private final ValidationModel expectedModel;
    private final ValidationModel actualModel;

    private final ScenarioHandler scenarioHandler;
    private int curLineNumber = 0;

    private final AtomicBoolean isStarted = new AtomicBoolean(false);
    private boolean isSuccess = false;

    public Scenario(Validator validator,
                    ScheduleManager scheduleManager, String scheduleKey,
                    String id, CaseDto caseDto,
                    List<String> discardKeywords) {
        this.validator = validator;
        this.scheduleKey = scheduleKey;
        this.id = id;
        this.fileManager = new FileManager();

        this.expectedModel = new ValidationModelBuilder()
                .setFileName(caseDto.getExpectedFile())
                .setLastLineNumber(fileManager.getTotalLineNumber(caseDto.getExpectedFile()))
                .setCurLineNumber(0)
                .build();

        this.actualModel = new ValidationModelBuilder()
                .setFileName(caseDto.getActualFile())
                .setLastLineNumber(fileManager.getTotalLineNumber(caseDto.getActualFile()))
                .setCurLineNumber(0)
                .build();

        Job scenarioHandleJob = new JobBuilder()
                .setScheduleManager(scheduleManager)
                .setName(Scenario.class.getSimpleName() + "_" + id)
                .setInitialDelay(0)
                .setInterval(1)
                .setTimeUnit(TimeUnit.MILLISECONDS)
                .setPriority(1)
                .setTotalRunCount(1)
                .setIsLasted(false)
                .build();
        this.scenarioHandler = new ScenarioHandler(validator, scenarioHandleJob, discardKeywords);
    }

    public Validator getValidator() {
        return validator;
    }

    public String getId() {
        return id;
    }

    public ValidationModel getExpectedModel() {
        return expectedModel;
    }

    public ValidationModel getActualModel() {
        return actualModel;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
        validator.getValidationResult().apply(id, isSuccess);
    }

    public boolean start() {
        if (getIsStarted()) { return false; }
        setIsStarted(true);

        scenarioHandler.start(this);
        return scenarioHandler.getJob().getScheduleManager().startJob(
                scheduleKey,
                scenarioHandler.getJob()
        );
    }

    public void stop() {
        if (!getIsStarted()) { return; }

        Job scenarioHandleJob = scenarioHandler.getJob();
        scenarioHandleJob.getScheduleManager().stopJob(
                scenarioHandleJob.getScheduleUnitKey(),
                scenarioHandleJob
        );

        setIsStarted(false);
    }

    public boolean getIsStarted() {
        return isStarted.get();
    }

    public void setIsStarted(boolean isStarted) {
        this.isStarted.set(isStarted);
    }

    public int getCurLineNumber() {
        return curLineNumber;
    }

    public void setCurLineNumber(int curLineNumber) {
        this.curLineNumber = curLineNumber;
    }

    public FileManager getFileManager() {
        return fileManager;
    }

    @Override
    public String toString() {
        return "Scenario{" +
                "id='" + id + '\'' +
                ", expectedModel=" + expectedModel +
                ", actualModel=" + actualModel +
                ", curLineNumber=" + curLineNumber +
                ", isStarted=" + isStarted +
                ", isSuccess=" + isSuccess +
                '}';
    }
}
