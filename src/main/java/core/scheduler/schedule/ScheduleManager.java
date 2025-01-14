package core.scheduler.schedule;

import core.scheduler.job.Job;
import core.scheduler.schedule.unit.ScheduleUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

public class ScheduleManager {

    ////////////////////////////////////////////////////////////
    // VARIABLES
    private static final Logger logger = LoggerFactory.getLogger(ScheduleManager.class);
    private final HashMap<String, ScheduleUnit> scheduleUnitMap = new HashMap<>();
    private final ReentrantLock scheduleUnitMapLock = new ReentrantLock();
    ////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////
    // CONSTRUCTOR
    public ScheduleManager() {}
    ////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////
    // FUNCTIONS
    public int getScheduleUnitMapSize() {
        return scheduleUnitMap.size();
    }

    public Map<String, ScheduleUnit> getCloneCallMap( ) {
        HashMap<String, ScheduleUnit> cloneMap;

        try {
            scheduleUnitMapLock.lock();

            cloneMap = (HashMap<String, ScheduleUnit>) scheduleUnitMap.clone();
        } catch (Exception e) {
            logger.warn("Fail to clone the schedule unit map.",  e);
            cloneMap = scheduleUnitMap;
        } finally {
            scheduleUnitMapLock.unlock();
        }

        return cloneMap;
    }

    private ScheduleUnit addScheduleUnit(String key, int poolSize, int queueSize) {
        if (key == null) {
            return null;
        }

        ScheduleUnit scheduleUnit = scheduleUnitMap.get(key);
        if (scheduleUnit != null) {
            return scheduleUnit;
        }

        try {
            scheduleUnitMapLock.lock();

            scheduleUnit = new ScheduleUnit(
                    key,
                    poolSize,
                    queueSize
            );
            scheduleUnitMap.put(key, scheduleUnit);
            return scheduleUnit;
        } catch (Exception e) {
            logger.warn("Fail to add the schedule unit.", e);
            return null;
        } finally {
            scheduleUnitMapLock.unlock();
        }
    }

    private void removeScheduleUnit(String key) {
        if (key == null) { return; }

        try {
            scheduleUnitMapLock.lock();
            Optional.ofNullable(scheduleUnitMap.get(key)).ifPresent(ScheduleUnit::stopAll);
            scheduleUnitMap.remove(key);
        } catch (Exception e) {
            logger.warn("Fail to delete the schedule unit map.", e);
        } finally {
            scheduleUnitMapLock.unlock();
        }
    }

    public ScheduleUnit getScheduleUnit(String key) {
        if (key == null) { return null; }

        return scheduleUnitMap.get(key);
    }

    public void clearScheduleUnitMap() {
        try {
            scheduleUnitMapLock.lock();
            scheduleUnitMap.values().forEach(ScheduleUnit::stopAll);
            scheduleUnitMap.clear();
            logger.debug("Success to clear the schedule unit map.");
        } catch (Exception e) {
            logger.warn("Fail to clear the schedule unit map.", e);
        } finally {
            scheduleUnitMapLock.unlock();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////

    public boolean initJob(String key, int totalThreadPoolSize, int priorityBlockingQueueSize) {
        return addScheduleUnit(key, totalThreadPoolSize, priorityBlockingQueueSize) != null;
    }

    public boolean startJob(String scheduleUnitKey, Job job) {
        if (scheduleUnitKey == null) { return false; }

        ScheduleUnit scheduleUnit = getScheduleUnit(scheduleUnitKey);
        if (scheduleUnit == null) {
            logger.warn("Fail to start the job. Fail to find the scheduleUnit. (scheduleUnitKey={})", scheduleUnitKey);
            return false;
        }

        return scheduleUnit.start(job);
    }

    public void stopJob(String scheduleUnitKey, Job job) {
        ScheduleUnit scheduleUnit = getScheduleUnit(scheduleUnitKey);
        if (scheduleUnit == null) {
            return;
        }

        scheduleUnit.stop(job);
    }

    public void stopAll(String scheduleUnitKey) {
        removeScheduleUnit(scheduleUnitKey);
    }

    public void finish() {
        clearScheduleUnitMap();
    }

    public int getActiveJobNumber(String scheduleUnitKey) {
        if (scheduleUnitKey == null) { return 0; }

        ScheduleUnit scheduleUnit = getScheduleUnit(scheduleUnitKey);
        if (scheduleUnit == null) {
            return 0;
        }

        return scheduleUnit.getJobListSize();
    }

    ////////////////////////////////////////////////////////////////////////////////

}
