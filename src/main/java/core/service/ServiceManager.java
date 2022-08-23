package core.service;

import core.config.ConfigManager;
import core.network.ResultNotiSender;
import core.scheduler.schedule.ScheduleManager;
import core.util.ScenarioParser;
import core.util.TimeUtil;
import core.validation.Validator;
import core.validation.scenario.Scenario;
import core.validation.scenario.model.CaseDto;
import core.validation.scenario.model.ScenarioDto;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static java.nio.file.StandardOpenOption.*;

@Slf4j
public class ServiceManager {

    ///////////////////////////////////////////////////
    public ServiceManager(ConfigManager configManager) {
        this.configManager = configManager;

        scheduleManager = new ScheduleManager();
        int threadPoolSize = configManager.getThreadPoolSize();
        if (!scheduleManager.initJob(MAIN_SCHEDULE_JOB, threadPoolSize, threadPoolSize * 2)) {
            log.error("Fail to initialize ScheduleManager.");
            System.exit(1);
        }

        validator = new Validator();

        Runtime.getRuntime().addShutdownHook(
                new ShutDownHookHandler("ShutDownHookHandler", Thread.currentThread(), this)
        );
    }
    ///////////////////////////////////////////////////

    ///////////////////////////////////////////////////
    public static final String MAIN_SCHEDULE_JOB = "MAIN";
    ///////////////////////////////////////////////////

    ///////////////////////////////////////////////////
    private final ConfigManager configManager;
    private final ScheduleManager scheduleManager;
    private final Validator validator;

    private final String tmpdir = System.getProperty("java.io.tmpdir");
    private final File lockFile = new File(tmpdir, System.getProperty("lock_file", "jdash_server.lock"));
    private FileChannel fileChannel;
    private FileLock lock;
    ///////////////////////////////////////////////////

    ///////////////////////////////////////////////////
    public boolean start() {
        systemLock();

        log.info("[UCHECK] START");

        while (validator.getTotalScenarioSize() != 0) {
            TimeUtil.sleep(1, TimeUnit.SECONDS);
        }

        return true;
    }

    public void register(String scenarioPath) {
        ScenarioParser scenarioParser = new ScenarioParser(scenarioPath);
        ScenarioDto scenarioDto = scenarioParser.readScenarioDto();
        if (scenarioDto == null) {
            log.error("Fail to read the scenario file.");
            return;
        }

        Map<String, Map<String, Object>> scenarios = scenarioDto.getScenarios();
        scenarios.forEach((key, value) -> {
            CaseDto caseDto = scenarioParser.readCaseDto(value);
            if (caseDto != null) {
                Scenario scenario = new Scenario(
                        validator,
                        scheduleManager, MAIN_SCHEDULE_JOB,
                        key, caseDto,
                        configManager.getDiscardKeywords()
                );
                if (validator.addScenario(scenario)) {
                    validator.getValidationResult().incAndGetTotalScenarioCount();
                    log.info("(Id:{}) Scenario: {}", key, validator.getScenario(key));
                }
            }
        });
    }
    
    public void stop() {
        scheduleManager.stopAll(MAIN_SCHEDULE_JOB);
        validator.clear();

        log.info("[RESULT]\n{}", validator.getValidationResult().getResults());
        validator.getValidationResult().save(configManager.getResultFilePath());

        // NOTIFY VALIDATION RESULT
        String notiUrl = configManager.getNotiUrl().trim();
        try {
            ResultNotiSender resultNotiSender = new ResultNotiSender(notiUrl);
            resultNotiSender.send(validator.getValidationResult().getResults());
        } catch (Exception e) {
            log.warn("Fail to send the noti to [{}]", notiUrl, e);
        }

        systemUnLock();
        log.info("[UCHECK] STOP");
    }
    ///////////////////////////////////////////////////

    ///////////////////////////////////////////////////
    public Validator getValidator() {
        return validator;
    }
    ///////////////////////////////////////////////////

    ///////////////////////////////////////////////////
    private void systemLock () {
        try {
            fileChannel = FileChannel.open(lockFile.toPath(), CREATE, READ, WRITE);
            lock = fileChannel.tryLock();
            if (lock == null) {
                log.error("DASH process is already running.");
                Thread.sleep(500L);
                System.exit(1);
            }
        } catch (Exception e) {
            log.error("ServiceManager.systemLock.Exception.", e);
        }
    }

    private void systemUnLock () {
        try {
            if (lock != null) {
                lock.release();
            }

            if (fileChannel != null) {
                fileChannel.close();
            }

            Files.delete(lockFile.toPath());
        } catch (IOException e) {
            //ignore
        }
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    /**
     * @class private static class ShutDownHookHandler extends Thread
     * @brief Graceful Shutdown 을 처리하는 클래스
     * Runtime.getRuntime().addShutdownHook(*) 에서 사용됨
     */
    private static class ShutDownHookHandler extends Thread {

        // shutdown 로직 후에 join 할 thread
        private final Thread target;
        private final ServiceManager serviceManager;

        public ShutDownHookHandler (String name, Thread target, ServiceManager serviceManager) {
            super(name);

            this.target = target;
            this.serviceManager = serviceManager;

            String currentHandlerName = ShutDownHookHandler.class.getSimpleName() + "_" + UUID.randomUUID().toString().substring(0, 8);
            this.setName(currentHandlerName);
            log.debug("| ShutDownHookHandler({}) is initiated. (target={})", currentHandlerName, target.getName());
        }

        /**
         * @fn public void run ()
         * @brief 정의된 Shutdown 로직을 수행하는 함수
         */
        @Override
        public void run ( ) {
            try {
                shutDown();
                target.join();
                log.debug("| ShutDownHookHandler({})'s target is finished successfully. (target={})", this.getName(), target.getName());
            } catch (Exception e) {
                log.warn("| ShutDownHookHandler({}).run.Exception", this.getName(), e);
            }
        }

        /**
         * @fn private void shutDown ()
         * @brief Runtime 에서 선언된 Handler 에서 사용할 서비스 중지 함수
         */
        private void shutDown ( ) {
            log.warn("| ({}) Process is about to quit. (Ctrl+C)", this.getName());
            serviceManager.stop();
        }
    }
    ///////////////////////////////////////////////////


}
